package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.dto.lost.*;
import GBFH.GBFH_BE.entity.main.Applicant;
import GBFH.GBFH_BE.entity.main.BoardFile;
import GBFH.GBFH_BE.entity.sub.Comment;
import GBFH.GBFH_BE.entity.sub.ImageFile;
import GBFH.GBFH_BE.entity.sub.LostBoard;
import GBFH.GBFH_BE.entity.sub.Status;
import GBFH.GBFH_BE.exception.*;
import GBFH.GBFH_BE.mapper.FileMapper;
import GBFH.GBFH_BE.repository.main.ApplicantRepository;
import GBFH.GBFH_BE.repository.main.BoardFileRepository;
import GBFH.GBFH_BE.repository.main.BoardRepository;
import GBFH.GBFH_BE.repository.sub.CommentRepository;
import GBFH.GBFH_BE.repository.sub.ImageFileRepository;
import GBFH.GBFH_BE.repository.sub.LostBoardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@org.springframework.transaction.annotation.Transactional("subTransactionManager")

public class LostService {
    private final LostBoardRepository lostBoardRepository;
    private final ApplicantRepository applicantRepository;
    private final ImageFileRepository imageFileRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;
    private final CommentService commentService;

    @Transactional("subTransactionManager")
    public CreateLostDTO.Res createLost(CreateLostDTO createLostDTO, String username, String clientIp, List<MultipartFile> files) throws UnknownHostException {
        // 사용자 정보 조회
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // S3에 업로드 및 URL 리스트 반환
        List<String> urls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            urls = files.stream().map(multipartFile -> {
                try {
                    String url = s3Uploader.upload(multipartFile, "lost");
                    log.info("S3 업로드 성공: {}", url);
                    return url;
                } catch (IOException e) {
                    log.error("S3 업로드 실패: {}", multipartFile.getOriginalFilename(), e);
                    throw new RuntimeException("S3 업로드 중 오류 발생", e);
                }
            }).toList();
        } else {
            log.info("업로드할 파일이 제공되지 않았습니다.");
        }

        // 첫 번째 URL을 대표 이미지로 사용
        String firstUrl = urls.isEmpty() ? null : urls.get(0);

        // LostBoard 생성 및 저장
        LostBoard lost = CreateLostDTO.mapToBoard(createLostDTO, user.getNameKor(), user.getUserNo(), clientIp, firstUrl);
        LostBoard savedLost = lostBoardRepository.save(lost);

        // ImageFile 엔티티 생성 및 LostBoard 연관 설정
        List<ImageFile> boardFiles = new ArrayList<>();
        long seq = 1;
        for (String url : urls) {
            ImageFile file = ImageFile.builder()
                    .fileId(url)
                    .seq(seq++)
                    .createIp(clientIp)
                    .lostBoard(savedLost)  // 연관 설정
                    .build();
            boardFiles.add(file);
        }

        // 파일 정보 저장
        imageFileRepository.saveAll(boardFiles);

        // DTO 변환 후 응답 반환
        List<FileDTO> fileDTOList = boardFiles.stream()
                .map(FileDTO::toDTO)
                .collect(Collectors.toList());

        return CreateLostDTO.Res.mapToDTO(savedLost, fileDTOList);
    }

    // 캐싱 도입해야 할 것 같음
    @Transactional(readOnly = true, transactionManager = "subTransactionManager")
    public List<GetLostDTO.LIST> getAllLosts() {
        List<LostBoard> lostBoardList = lostBoardRepository.findAllByTrashYNOrderByIdxDesc('N');

        // status 별로 그룹화 후, 상위 5개만 선택
        Map<String, List<GetLostDTO.ListItem>> groupedByStatus = lostBoardList.stream()
                .collect(Collectors.groupingBy(
                        LostBoard::getStatus,
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




    @Transactional(readOnly = true, transactionManager = "subTransactionManager")
    public GetLostDTO.DETAIL getDetailLost(Long boardIdx, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        LostBoard lost = lostBoardRepository.findByIdxAndTrashYN(boardIdx, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 조회수 증가
        lost.readBoard();
        
        // lost에서 연결된 이미지 가져오기
        List<ImageFile> files = lost.getImageFiles();
        List<FileDTO> fileDTOS = files.stream().map(FileDTO::toDTO).toList();

//        if (!lost.getBoardId().toString().equals("lost"))
//            throw new NotLostException("분실물 글이 아닙니다.");

        Boolean permission = user.getUserNo().equals(lost.getCreateId());

        // 댓글 불러오기
        List<GetCommentDTO> commentDTOS = commentService.getCommentDTOSWithBoardIdx(boardIdx, user);

        return GetLostDTO.DETAIL.mapToDTO(lost, permission, fileDTOS, commentDTOS);
    }


    @Transactional(readOnly = true, transactionManager = "subTransactionManager")
    public List<GetLostDTO.CategoryList> getLostsByStatus(String status) {
        List<LostBoard> lostBoardList = lostBoardRepository.findAllByTrashYNAndStatusOrderByIdxDesc('N', Status.valueOf(status));

        return lostBoardList.stream().map(GetLostDTO.CategoryList::mapToDTO).collect(Collectors.toList());
    }



    @Transactional("subTransactionManager")
    public void deleteLost(Long boardId, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        LostBoard lost = lostBoardRepository.findByIdxAndTrashYN(boardId, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 글 작성자인가?
        if(!lost.getCreateId().equals(user.getUserNo())) {
            throw new NoPermissionException("작성자가 아닙니다.");
        }

        // 댓글 조회
        List<Comment> comments = commentRepository.findByLostBoardIdxAndDelYN(boardId, "N");
        // 댓글 삭제 처리
        comments.forEach(Comment::delete);

        lost.delete();
    }

    @Transactional("subTransactionManager")
    public UpdateLostStatusDTO.Res updateLost(Long boardIdx, String username, UpdateLostStatusDTO updateLostStatusDTO) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        LostBoard lost = lostBoardRepository.findByIdxAndTrashYN(boardIdx, 'N')
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        // 글 작성자인가?
        if(!lost.getCreateId().equals(user.getUserNo())) {
            throw new NoPermissionException("작성자가 아닙니다.");
        }

        // 상태 변경
        lost.updateStatus(updateLostStatusDTO);
        return UpdateLostStatusDTO.mapToDTO(lost);
    }

    @Transactional("subTransactionManager")
    public UpdateLostContentDTO.Res updateLostContent(Long boardId, String username, UpdateLostContentDTO updateLostContentDTO, List<MultipartFile> files, List<String> deleteFiles, String clientIp) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        LostBoard lost = lostBoardRepository.findByIdxAndTrashYN(boardId, 'N')
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
                ImageFile file = imageFileRepository.findByFileId(fileId)
                        .orElseThrow(() -> new FileNotFoundException("이미지를 찾을 수 없습니다."));

                // s3에 이미지 삭제
                s3Uploader.deleteFile(fileId);

                // 데이터베이스에서 이미지 데이터 삭제
                imageFileRepository.delete(file);
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

        List<ImageFile> boardFiles = new ArrayList<>();

        // 데이터베이스 저장
        if(!urls.isEmpty()) {
            // boardId 파일 중 가장 큰 seq 값 + 1
            Long fileSeq = imageFileRepository.findMaxGrpByIdx(boardId) + 1;

            for(String url : urls) {
                ImageFile boardFile = ImageFile.builder()
                        .fileId(url)
                        .seq(fileSeq++)
                        .createIp(clientIp)
                        .lostBoard(lost)
                        .build();

                boardFiles.add(boardFile);
            }
        }

        imageFileRepository.saveAll(boardFiles);

        // 전체 조회
        List<ImageFile> savedFiles  = imageFileRepository.findAllByIdx(boardId);
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
        List<LostBoard> losts = lostBoardRepository.findByTitleOrContentsContainingAndOrderByCreateDTDesc(q);

        return losts.stream().map(GetLostDTO.CategoryList::mapToDTO).toList();
    }
}
