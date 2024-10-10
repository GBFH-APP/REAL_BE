package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardId;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
        private final BoardRepository boardRepository;
        private final BoardFileService boardFileService;
        private final BoardIdService boardIdService;
        private final PaginateService paginateService;

        public NoticeResponseDTO getNotice(Long id) {
                Board notice = boardRepository.findByIdx(id)
                        .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

                boardRepository.save(notice.readBoard()); // 조회수 올림
                //파일 추가 필요
                if (boardFileService.isExistFile(id)) {
                        return NoticeResponseDTO.toDTO(notice, boardFileService.getAllFileDTO(id));
                }
                else {
                        return NoticeResponseDTO.toDTO(notice, null);
                }
        }

        public List<NoticeResponseDTO> getAllNoticeSpeak(String category) {
                // category 있는지 없는지 확인 후, 예외처리
                BoardId boardId = boardIdService.getCategory(category);


                List<Board> noticeList;
                if (boardId.name().equals("recruitments")) {
                        noticeList = boardRepository.findAllByTitleContainingAndBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc("채용", BoardId.notice, 1, LocalDate.now().toString(), LocalDate.now().toString());
                }
                else {
                        noticeList = boardRepository.findAllByBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(boardId, 1, LocalDate.now().toString(), LocalDate.now().toString());
                }


                if (noticeList.isEmpty()) {
                        throw new EmptyPostException("글이 비었습니다.");
                }
                // noti 1이고 오늘 날짜가 noti_start랑 noti_end에 끼어있으면 먼저 내보냄
                return noticeList.stream().map(notice ->{
                        // 첨부파일 받아오기
                        if (boardFileService.isExistFile(notice.getIdx())) {
                                return NoticeResponseDTO.toDTO(notice, boardFileService.getAllFileDTO(notice.getIdx()));
                        }
                        else {
                                return NoticeResponseDTO.toDTO(notice, null);
                        }
                }).collect(Collectors.toList());

        }

        public Page<NoticeResponseDTO> getAllNotice(String category, int page, int size) {
                // category 있는지 없는지 확인 후, 예외처리
                BoardId boardId = boardIdService.getCategory(category);

                List<Board> noticeList;
                if (boardId.name().equals("recruitments")) {
                        noticeList = boardRepository.findAllByTitleContainingAndBoardIdAndNotiOrderByCreateDTDesc("채용", BoardId.notice, 0);
                }
                else {
                        noticeList = boardRepository.findAllByBoardIdAndNotiOrderByCreateDTDesc(boardId, 0);
                }

                if (noticeList.isEmpty()) {
                        throw new EmptyPostException("글이 비었습니다.");
                }

                List<NoticeResponseDTO> noticeResponseDTO =  noticeList.stream().map(notice ->{
                        // 첨부파일 받아오기
                        if (boardFileService.isExistFile(notice.getIdx())) {
                                return NoticeResponseDTO.toDTO(notice, boardFileService.getAllFileDTO(notice.getIdx()));
                        }
                        else {
                                return NoticeResponseDTO.toDTO(notice, null);
                        }
                }).toList();

                return paginateService.paginateList(noticeResponseDTO, page, size);

        }
}
