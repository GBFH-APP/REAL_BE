package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ErrorCode;
import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.dto.response.ErrorResponseDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.exception.category_BoardIdNotFoundException;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.exception.WrongPaginationException;
import GBFH.GBFH_BE.repository.BoardRepository;
import GBFH.GBFH_BE.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping
    public List<Long> getBoard() {
        return boardRepository.findAll().stream().map(Board::getIdx).collect(Collectors.toList());
    }

//    @GetMapping("/all")
//    public ResponseEntity<Object> getAllNotice() {
//        return null;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> getNotice(@Valid @PathVariable Long id) {

            NoticeResponseDTO noticeResponseDTO = boardService.getNotice(id);
            return ResponseEntity
                    .status(ResponseCode.SUCCESS_NOTICE_RETRIEVE.getStatus().value())
                    .body(new ResponseDTO<>(ResponseCode.SUCCESS_NOTICE_RETRIEVE, noticeResponseDTO));
    }
    
    @GetMapping("/all/{category}/speak")
    public ResponseEntity<ResponseDTO<?>> getAllNoticeSpeak(@Valid @PathVariable String category) {
        List<NoticeResponseDTO> noticeResponseDTOList = boardService.getAllNoticeSpeak(category);
            return ResponseEntity
                    .status(ResponseCode.SUCCESS_NOTICE_RETRIEVE.getStatus().value())
                    .body(new ResponseDTO<>(ResponseCode.SUCCESS_NOTICE_RETRIEVE, noticeResponseDTOList));
    }

    @GetMapping("all/{category}/normal")
    public ResponseEntity<ResponseDTO<?>> getAllNotice(@Valid @PathVariable String category,
                                               @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Page<NoticeResponseDTO> noticeResponseDTOList = boardService.getAllNotice(category, page, size);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_NOTICE_RETRIEVE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_NOTICE_RETRIEVE, noticeResponseDTOList));
    }
}
