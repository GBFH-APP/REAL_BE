package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.dto.lost.*;
import GBFH.GBFH_BE.entity.*;
import GBFH.GBFH_BE.exception.*;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.BoardFileRepository;
import GBFH.GBFH_BE.repository.BoardRepository;
import GBFH.GBFH_BE.repository.CommentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.ErrorState;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional

public class LostService {
    private final BoardRepository boardRepository;
    private final ApplicantRepository applicantRepository;
    private final BoardFileRepository boardFileRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public CreateLostDTO.Res createLost(CreateLostDTO createLostDTO, String username, String clientIp, List<MultipartFile> files) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        // s3에 업로드 후 url 리스트 반환
        List<String> urls = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            urls = files.stream().map(multipartFile -> {
                try {
                    String url = s3Uploader.upload(multipartFile, "lost");
                    log.info("S3 업로드 성공: " + url);
                    return url;
                } catch (IOException e) {
                    log.error("S3 업로드 실패: " + multipartFile.getOriginalFilename(), e);
                    throw new RuntimeException("S3 업로드 실패", e);
                }
            }).toList();
        } else {
            log.info("업로드할 파일이 제공되지 않았습니다.");
        }

        // BoardFile 저장
        // idx(max)로 파일 id 부여 > seq 자동 1 증가 > file_id에는 파일명 저장

        Long IDX = boardRepository.findMaxIdx() + 1;
        Long grp = boardRepository.findMaxGrp() + 1;

        List<BoardFile> boardFiles = new ArrayList<>();

        if(!urls.isEmpty()) {
            Long fileSeq = 1L;

            for(String url : urls) {
                BoardFile boardFile = BoardFile.builder()
                        .fileId(url)
                        .idx(IDX)
                        .seq(fileSeq++)
                        .createIp(clientIp)
                        .build();

                boardFiles.add(boardFile);
            }
        }

        boardFileRepository.saveAll(boardFiles);

        List<FileDTO> fileDTOList = boardFiles.stream().map(FileDTO::toDTO).toList();

        String url = urls.isEmpty() ? null : urls.get(0);

        // 첫 번째 file_id board에 있는 filee_id 필드에 저장
        try {
            Board lost = CreateLostDTO.mapToBoard(createLostDTO, IDX, grp, user.getNameKor(), user.getUserNo(), clientIp, url);
            Board savedLost = boardRepository.save(lost);
            return CreateLostDTO.Res.mapToDTO(savedLost, fileDTOList);

        } catch (UnknownHostException e) {
            throw new InvalidHostException("호스트 연결 실패");
        }
    }

    // 캐싱 도입해야 할 것 같음
    @Transactional(readOnly = true)
    public List<GetLostDTO.LIST> getAllLosts() {
        List<Board> losts = boardRepository.findAllByBoardIdAndTrashYNOrderByIdxDesc(BoardId.lost, 'N');

        // status 별로 그룹화 후, 상위 5개만 선택
        Map<String, List<GetLostDTO.ListItem>> groupedByStatus = losts.stream()
                .collect(Collectors.groupingBy(
                        Board::getStatus,
                        Collectors.collectingAndThen(
                                Collectors.mapping(GetLostDTO.ListItem::mapToDTO, Collectors.toList()),
                                list -> list.stream().limit(5).collect(Collectors.toList()) // 각 그룹에서 5개 제한
                        )
                ));

        // LIST DTO 로 변환하여 반환
        return groupedByStatus.entrySet().stream()
                .map(entry -> new GetLostDTO.LIST(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }




    @Transactional(readOnly = true)
    public GetLostDTO.DETAIL getDetailLost(Long id, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdxAndTrashYN(id, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 조회수 증가
        lost.readBoard();

        List<BoardFile> files = boardFileRepository.findAllByIdx(lost.getIdx());
        List<FileDTO> fileDTOS = files.stream().map(FileDTO::toDTO).toList();

        if (!lost.getBoardId().toString().equals("lost"))
            throw new NotLostException("분실물 글이 아닙니다.");

        Boolean permission = user.getUserNo().equals(lost.getCreateId());

        // 한 번의 쿼리로 lvl = 1 및 lvl = 2 댓글 가져오기
        List<Comment> allComments = commentRepository.findByUpIdxAndDelYN(id, "N");

        // lvl = 2 댓글을 grp 기준으로 Map에 저장 (Key: grp, Value: 대댓글 리스트)
        Map<Long, List<Comment>> replyMap = allComments.stream()
                .filter(comment -> comment.getLvl() == 2)
                .collect(Collectors.groupingBy(Comment::getGrp));

        // lvl = 1 댓글만 필터링 후 대댓글 매핑
        List<GetCommentDTO> commentDTOS = allComments.stream()
                .filter(comment -> comment.getLvl() == 1)
                .map(comment -> {
                    Boolean commentPermission = comment.getCreateId().equals(user.getUserNo());

                    // grp 기준으로 대댓글 리스트 가져오기 (없으면 빈 리스트)
                    List<GetReplyDTO> commentReplyDTOS = replyMap.getOrDefault(comment.getGrp(), Collections.emptyList())
                            .stream()
                            .map(reply -> {
                                Boolean replyPermission = reply.getCreateId().equals(user.getUserNo());
                                return GetReplyDTO.mapToReplyDTO(reply, replyPermission);
                            })
                            .toList();

                    return GetCommentDTO.mapToCommentDTO(comment, commentPermission, commentReplyDTOS);
                }).toList();

        return GetLostDTO.DETAIL.mapToDTO(lost, permission, fileDTOS, commentDTOS);
    }


    @Transactional(readOnly = true)
    public List<GetLostDTO.CategoryList> getLostsByStatus(String status) {
        List<Board> losts = boardRepository.findAllByBoardIdAndTrashYNAndStatusOrderByIdxDesc(BoardId.lost, 'N', status);

        return losts.stream().map(GetLostDTO.CategoryList::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public CreateCommentDTO.Res createComment(Long id, String username, CreateCommentDTO createCommentDTO, String clientId) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Long grp = commentRepository.findMaxGrp() + 1;

        Comment comment = CreateCommentDTO.mapToComment(createCommentDTO, id, grp, user.getNameKor(), user.getUserNo(), clientId);
        Comment savedComment = commentRepository.save(comment);

        return CreateCommentDTO.Res.mapToDTO(savedComment);
    }

    @Transactional
    public CreateCommentDTO.Res createReply(Long boardId, Long commentId, String username, @Valid CreateCommentDTO createCommentDTO, String clientIp) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Comment comment = commentRepository.findByIdxAndDelYN(commentId, "N")
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        Long grp = comment.getGrp(); // 댓글과 동일한 그룹을 가짐

        Comment createdComment = CreateCommentDTO.mapToCommentReply(createCommentDTO, boardId, grp, user.getNameKor(), user.getUserNo(), clientIp);
        Comment savedComment = commentRepository.save(createdComment);

        return CreateCommentDTO.Res.mapToDTO(savedComment);
    }

    @Transactional
    public void deleteCommentReply(Long boardId, Long commentId, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Comment comment = commentRepository.findByIdxAndDelYN(commentId, "N")
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        // 글 작성자인가?
        if(!comment.getCreateId().equals(user.getUserNo())) {
            throw new NoPermissionException("작성자가 아닙니다.");
        }

        comment.delete();
        // 상위 댓글인가? (대댓글이 아닌가?)
        if(comment.getLvl() == 1L) {
            // 대댓글 조회
            List<Comment> commentReplies = commentRepository.findByUpIdxAndDelYNAndLvlAndGrp(boardId, "N", 2L, comment.getGrp());
            commentReplies.forEach(Comment::delete); // 대댓글 모두 휴지통 처리
        }
    }

    @Transactional
    public void deleteLost(Long boardId, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdxAndTrashYN(boardId, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 글 작성자인가?
        if(!lost.getCreateId().equals(user.getUserNo())) {
            throw new NoPermissionException("작성자가 아닙니다.");
        }

        // 댓글 조회
        List<Comment> comments = commentRepository.findByUpIdxAndDelYN(boardId, "N");
        // 댓글 삭제 처리
        comments.forEach(Comment::delete);

        lost.delete();
    }

    @Transactional
    public UpdateLostStatusDTO.Res updateLost(Long boardId, String username, UpdateLostStatusDTO updateLostStatusDTO) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdxAndTrashYN(boardId, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 글 작성자인가?
        if(!lost.getCreateId().equals(user.getUserNo())) {
            throw new NoPermissionException("작성자가 아닙니다.");
        }

        // 상태 변경
        lost.updateStatus(updateLostStatusDTO);
        return UpdateLostStatusDTO.mapToDTO(lost);
    }

    @Transactional
    public UpdateLostContentDTO.Res updateLostContent(Long boardId, String username, UpdateLostContentDTO updateLostContentDTO, List<MultipartFile> files, List<String> deleteFiles, String clientIp) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdxAndTrashYN(boardId, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 글 작성자인가?
        if(!lost.getCreateId().equals(user.getUserNo())) {
            throw new NoPermissionException("작성자가 아닙니다.");
        }

        // 내용 업데이트
        lost.updateContent(updateLostContentDTO);

        // imageUrls 비어있지 않다면 -> 삭제
        // 해당 url 이미지 객체를 찾아서 삭제, s3에도 삭제
        // 아예 삭제해버림!

        if(deleteFiles != null) {
            System.out.println("in");
            deleteFiles.forEach(fileId -> {
                BoardFile file = boardFileRepository.findByFileId(fileId)
                        .orElseThrow(() -> new FileNotFoundException("이미지를 찾을 수 없습니다."));

                // s3에 이미지 삭제
                s3Uploader.deleteFile(fileId);

                // 데이터베이스에서 이미지 데이터 삭제
                boardFileRepository.delete(file);
            });
        }


        // files가 비어있지 않다면 -> 새로 추가
        List<String> urls = new ArrayList<>();
        if(files != null && !files.isEmpty()) {

            if (files != null) {
                urls = files.stream().map(multipartFile -> {
                    try {
                        String url = s3Uploader.upload(multipartFile, "lost");
                        log.info("S3 업로드 성공: " + url);
                        return url;
                    } catch (IOException e) {
                        log.error("S3 업로드 실패: " + multipartFile.getOriginalFilename(), e);
                        throw new RuntimeException("S3 업로드 실패", e);
                    }
                }).toList();
            } else {
                log.info("업로드할 파일이 제공되지 않았습니다.");
            }
        }

        List<BoardFile> boardFiles = new ArrayList<>();

        // 데이터베이스 저장
        if(!urls.isEmpty()) {
            // boardId 파일 중 가장 큰 seq 값 + 1
            Long fileSeq = boardFileRepository.findMaxGrpByIdx(boardId) + 1;

            for(String url : urls) {
                BoardFile boardFile = BoardFile.builder()
                        .fileId(url)
                        .idx(boardId)
                        .seq(fileSeq++)
                        .createIp(clientIp)
                        .build();

                boardFiles.add(boardFile);
            }
        }

        boardFileRepository.saveAll(boardFiles);

        // 전체 조회
        List<BoardFile> savedFiles  = boardFileRepository.findAllByIdx(boardId);
        System.out.println("savedFiles : " + savedFiles);
        List<FileDTO> fileDTOList = savedFiles.stream().map(FileDTO::toDTO).toList();

        // 첫 이미지가 변경되었는가?
        try {
            if (!savedFiles.isEmpty()) {
                String newFileId = savedFiles.get(0).getFileId();
                String oldFileId = lost.getFileId();

                if (oldFileId == null || !oldFileId.equals(newFileId)) {
                    lost.updateTitleImage(newFileId);
                }
            } else {
                lost.updateTitleImage(null); // 기본 이미지가 없다면 null 처리
            }
        } catch (Exception e) {
            throw new PageTitleException("페이지 타이틀 갱신 중 에러 발생");
        }


        // 변경된 내용 반영하여 응답
        return  UpdateLostContentDTO.Res.mapToDTO(lost, fileDTOList);
    }

    public List<GetLostDTO.CategoryList> getAllBySearch(String q) {
        // 분실물 검색 일단 title과 content에 문자열을 포함하는 조건으로 진행함
        List<Board> losts = boardRepository.findByBoardIdAndTitleOrContentsContainingAndOrderByCreateDTDesc(BoardId.lost, q);

        return losts.stream().map(GetLostDTO.CategoryList::mapToDTO).toList();
    }
}
