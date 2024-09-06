package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
        private final BoardRepository boardRepository;

        public NoticeResponseDTO getNotice(Long id) {
                Board notice = boardRepository.findByIDX(id)
                        .orElseThrow(() -> new RuntimeException("찾는 글이 없습니다."));

                notice.readNotice(); // 조회수 올림
                //파일 추가 필요
                return NoticeResponseDTO.toDTO(notice);
        }

//        public List<NoticeResponseDTO> getAllNotice() {
//
//        }
}
