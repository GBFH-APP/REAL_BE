package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ErrorCode;
import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.dto.response.ErrorResponseDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.repository.BoardRepository;
import GBFH.GBFH_BE.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping
    public List<Long> getBoard() {
        return boardRepository.findAll().stream().map(Board::getIDX).collect(Collectors.toList());
    }

//    @GetMapping("/all")
//    public ResponseEntity<Object> getAllNotice() {
//        return null;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNotice(@PathVariable Long id) {
        try {
            System.out.println(id);
            NoticeResponseDTO noticeResponseDTO = boardService.getNotice(id);
            return ResponseEntity
                    .status(ResponseCode.SUCCESS_NOTICE_RETRIEVE.getStatus().value())
                    .body(new ResponseDTO<>(ResponseCode.SUCCESS_NOTICE_RETRIEVE, noticeResponseDTO));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUNT_POST.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.NOT_FOUNT_POST));
        }
    }
}
