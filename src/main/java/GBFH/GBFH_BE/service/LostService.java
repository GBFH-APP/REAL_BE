package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileResponseDTO;
import GBFH.GBFH_BE.dto.lost.CreateLostDTO;
import GBFH.GBFH_BE.dto.lost.GetLostDTO;
import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardFile;
import GBFH.GBFH_BE.entity.BoardId;
import GBFH.GBFH_BE.exception.InvalidHostException;
import GBFH.GBFH_BE.exception.NotLostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.BoardFileRepository;
import GBFH.GBFH_BE.repository.BoardRepository;
import jakarta.transaction.Transactional;
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

        Long IDX = boardRepository.findMaxIdx();
        Long grp = boardRepository.findMaxGrp();

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
            Board lost = CreateLostDTO.mapToBoard(createLostDTO, IDX + 1, grp + 1, user.getNameKor(), user.getUserNo(), clientIp, url);
            Board savedLost = boardRepository.save(lost);
            return CreateLostDTO.Res.mapToDTO(savedLost, fileDTOList);

        } catch (UnknownHostException e) {
            throw new InvalidHostException("호스트 연결 실패");
        }
    }

    public List<GetLostDTO.LIST> getAllLosts() {
        List<Board> losts = boardRepository.findAlByBoardIdOrderByIdxDesc(BoardId.lost);

        return losts.stream().map(GetLostDTO.LIST::mapToDTO).collect(Collectors.toList());
    }

    public GetLostDTO.DETAIL getDetailLost(Long id, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdx(id)
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        if(!lost.getBoardId().toString().equals("lost"))
            throw new NotLostException("분실물 글이 아닙니다.");

        Boolean permission = user.getUserNo().equals(lost.getCreateId());

        return GetLostDTO.DETAIL.mapToDTO(lost, permission);
    }

    public List<GetLostDTO.LIST> getLostsByStatus(String status) {
        List<Board> losts = boardRepository.findAllByBoardIdAndStatusOrderByIdxDesc(BoardId.lost, status);

        return losts.stream().map(GetLostDTO.LIST::mapToDTO).collect(Collectors.toList());
    }
}
