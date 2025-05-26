package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.board.NoticeListResponseDTO;
import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.dto.board.SimplePostDTO;
import GBFH.GBFH_BE.entity.main.Board;
import GBFH.GBFH_BE.entity.main.BoardId;
import GBFH.GBFH_BE.entity.main.BoardSummary;
import GBFH.GBFH_BE.entity.main.SimpleNotice;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.main.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional("mainTransactionManager")
public class BoardService {
        private final BoardRepository boardRepository;
        private final BoardFileService boardFileService;
        private final BoardIdService boardIdService;
        private final PaginateService paginateService;

        public NoticeResponseDTO getNotice(Long id) {
                Board notice = boardRepository.findByIdx(id)
                        .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

                boardRepository.save(notice.readBoard()); // 조회수 올림

                SimplePostDTO prev = null;
                SimplePostDTO next1 = null;
                if (notice.getTitle().contains("채용")) {
                        // 이전글 (채용 포함)
                        SimpleNotice previous = boardRepository.findFirstByBoardIdAndCreateDTBeforeAndTitleContainingOrderByCreateDTDesc(
                                notice.getBoardId(), notice.getCreateDT(), "채용");
                        if (previous != null) {
                                prev = SimplePostDTO.builder()
                                        .idx(previous.getIdx().toString())
                                        .title(previous.getTitle())
                                        .build();
                        }

                        // 다음글 (채용 포함)
                        SimpleNotice next = boardRepository.findFirstByBoardIdAndCreateDTAfterAndTitleContainingOrderByCreateDTAsc(
                                notice.getBoardId(), notice.getCreateDT(), "채용");
                        if (next != null) {
                                next1 = SimplePostDTO.builder()
                                        .idx(next.getIdx().toString())
                                        .title(next.getTitle())
                                        .build();
                        }

                } else {
                        // 이전글 (채용 제외)
                        SimpleNotice previous = boardRepository.findFirstByBoardIdAndCreateDTBeforeAndTitleNotContainingOrderByCreateDTDesc(
                                notice.getBoardId(), notice.getCreateDT(), "채용");
                        if (previous != null) {
                                prev = SimplePostDTO.builder()
                                        .idx(previous.getIdx().toString())
                                        .title(previous.getTitle())
                                        .build();
                        }

                        // 다음글 (채용 제외)
                        SimpleNotice next = boardRepository.findFirstByBoardIdAndCreateDTAfterAndTitleNotContainingOrderByCreateDTAsc(
                                notice.getBoardId(), notice.getCreateDT(), "채용");
                        if (next != null) {
                                next1 = SimplePostDTO.builder()
                                        .idx(next.getIdx().toString())
                                        .title(next.getTitle())
                                        .build();
                        }
                }

                // 파일 추가 필요
                if (boardFileService.isExistFile(id)) {
                        return NoticeResponseDTO.toDTO(notice, boardFileService.getAllFileDTO(id),prev, next1);
                } else {
                        return NoticeResponseDTO.toDTO(notice, null,prev, next1);
                }
        }


        public List<NoticeListResponseDTO> getAllNoticeSpeak(String category) {
                // category 있는지 없는지 확인 후, 예외처리
                BoardId boardId = boardIdService.getCategory(category);


                List<BoardSummary> noticeList;
                if (boardId.name().equals("recruitments")) {
                        noticeList = boardRepository.findAllByTitleContainingAndBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc("채용", BoardId.notice, 1, LocalDate.now().toString(), LocalDate.now().toString());
                }
                else {
                        noticeList = boardRepository.findAllByBoardIdAndNotiAndTitleNotContainingAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(
                                boardId, 1, "채용", LocalDate.now().toString(), LocalDate.now().toString());
                }


                if (noticeList.isEmpty()) {
                        throw new EmptyPostException("글이 비었습니다.");
                }
                // noti 1이고 오늘 날짜가 noti_start랑 noti_end에 끼어있으면 먼저 내보냄
                return noticeList.stream().map(NoticeListResponseDTO::toSummaryDTO).collect(Collectors.toList());

        }


        public Page<NoticeListResponseDTO> getAllNotice(String category, int page, int size) {
                BoardId boardId = boardIdService.getCategory(category);

                LocalDate today = LocalDate.now();
                List<BoardSummary> noticeList;

                if (boardId.name().equals("recruitments")) {
                        noticeList = boardRepository.findByBoardIdAndTitleContainingAndNotiCondition(
                                BoardId.notice, "채용", today.toString());
                } else {
                        noticeList = boardRepository.findByBoardIdAndTitleNotContainingAndNotiCondition(
                                boardId, "채용", today.toString());
                }

                if (noticeList.isEmpty()) {
                        throw new EmptyPostException("글이 비었습니다.");
                }

                List<NoticeListResponseDTO> noticeResponseDTO = noticeList.stream()
                        .map(NoticeListResponseDTO::toSummaryDTO)
                        .collect(Collectors.toList());

                return paginateService.paginateList(noticeResponseDTO, page, size);
        }
}
