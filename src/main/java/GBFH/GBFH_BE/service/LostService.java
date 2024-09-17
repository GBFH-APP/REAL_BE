package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileResponseDTO;
import GBFH.GBFH_BE.dto.lost.*;
import GBFH.GBFH_BE.entity.*;
import GBFH.GBFH_BE.exception.CommentNotFoundException;
import GBFH.GBFH_BE.exception.InvalidHostException;
import GBFH.GBFH_BE.exception.NotLostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.BoardFileRepository;
import GBFH.GBFH_BE.repository.BoardRepository;
import GBFH.GBFH_BE.repository.CommentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
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

        System.out.println(urls);

        List<BoardFile> boardFiles = new ArrayList<>();

        if(!urls.isEmpty()) {
            Long fileSeq = 1L;

            for(String url : urls) {
                System.out.println("in");
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

        List<FileResponseDTO.FileDTO> fileDTOList = boardFiles.stream().map(FileResponseDTO::toDTO).toList();

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

    public List<GetLostDTO.LIST> getAllLosts() {
        List<Board> losts = boardRepository.findAllByBoardIdAndTrashYNOrderByIdxDesc(BoardId.lost, 'N');
        return losts.stream().map(GetLostDTO.LIST::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public GetLostDTO.DETAIL getDetailLost(Long id, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdxAndTrashYN(id, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 조회수 증가
        lost.readBoard();

        List<BoardFile> files = boardFileRepository.findAllByIdx(lost.getIdx());
        List<FileResponseDTO.FileDTO> fileDTOS = files.stream().map(FileResponseDTO::toDTO).toList();

        if(!lost.getBoardId().toString().equals("lost"))
            throw new NotLostException("분실물 글이 아닙니다.");

        Boolean permission = user.getUserNo().equals(lost.getCreateId());

        // 댓글 모두 가져오기 - lvl = 1
        List<Comment> comments = commentRepository.findByUpIdxAndDelYNAndLvl(id, "N", 1L);

        // 댓글 수정 권한 확인 permission 추가 (true이면 내가 작성한 글, false 이면 내가 작성하지 않은 글)
        List<GetCommentDTO> commentDTOS = comments.stream().map(comment -> {
            Boolean commentPermission = comment.getCreateId().equals(user.getUserNo());

            // 대댓글 조회
            List<Comment> commentReplies = commentRepository.findByUpIdxAndDelYNAndLvlAndGrp(id, "N", 2L, comment.getGrp());
            List<GetReplyDTO> commentReplyDTOS = commentReplies.stream().map(reply -> {
                Boolean replyPermission = comment.getCreateId().equals(user.getUserNo());
                return GetReplyDTO.mapToReplyDTO(reply, replyPermission);
            }).toList();

            return GetCommentDTO.mapToCommentDTO(comment, commentPermission, commentReplyDTOS);
        }).toList();

        return GetLostDTO.DETAIL.mapToDTO(lost, permission, fileDTOS, commentDTOS);
    }

    public List<GetLostDTO.LIST> getLostsByStatus(String status) {
        List<Board> losts = boardRepository.findAllByBoardIdAndStatusOrderByIdxDesc(BoardId.lost, status);

        return losts.stream().map(GetLostDTO.LIST::mapToDTO).collect(Collectors.toList());
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

        // 삭제 가능한가?
        if(comment.getCreateId().equals(user.getUserNo())) {
            comment.delete();
            // 상위 댓글인가? (대댓글이 아닌가?)
            if(comment.getLvl() == 1L) {
                // 대댓글 조회
                List<Comment> commentReplies = commentRepository.findByUpIdxAndDelYNAndLvlAndGrp(boardId, "N", 2L, comment.getGrp());
                commentReplies.forEach(Comment::delete); // 대댓글 모두 휴지통 처리
            }
        }
    }

    @Transactional
    public void deleteLost(Long boardId, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdxAndTrashYN(boardId, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 자신이 작성한 글인가?
        if(user.getUserNo().equals(lost.getCreateId())) {
            // 댓글 조회
            List<Comment> comments = commentRepository.findByUpIdxAndDelYNAndLvl(boardId, "N", 1L);
            comments.forEach(comment -> {
                // 대댓글 조회
                List<Comment> commentReplies = commentRepository.findByUpIdxAndDelYNAndLvlAndGrp(boardId, "N", 2L, comment.getGrp());
                // 대댓글 모두 삭제
                commentReplies.forEach(Comment::delete);
                // 댓글 삭제
                comment.delete();
            });
        }

        lost.delete();
    }
}
