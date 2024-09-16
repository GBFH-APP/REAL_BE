package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
        private final BoardRepository boardRepository;
        private final BoardFileService boardFileService;

        public NoticeResponseDTO getNotice(Long id) {
                Board notice = boardRepository.findByIdx(id)
                        .orElseThrow(() -> new RuntimeException("찾는 글이 없습니다."));

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
                List<Board> noticeList = boardRepository.findAllByBoardIdAndNotiAndNotiEndAfterAndNotiStartBefore(category, 1, LocalDate.now().toString(), LocalDate.now().toString());
                // noti 1이고 오늘 날짜가 noti_start랑 noti_end에 끼어있으면 먼저 내보냄
                return noticeList.stream().map(notice ->{
                    return NoticeResponseDTO.toDTO(notice, null);
                }).collect(Collectors.toList());

        }
}
