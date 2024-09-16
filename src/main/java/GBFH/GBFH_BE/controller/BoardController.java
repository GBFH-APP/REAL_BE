package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ErrorCode;
import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.dto.response.ErrorResponseDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardId;
import GBFH.GBFH_BE.exception.BoardIdNotFountException;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.exception.WrongPaginationException;
import GBFH.GBFH_BE.repository.BoardRepository;
import GBFH.GBFH_BE.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return boardRepository.findAll().stream().map(Board::getIdx).collect(Collectors.toList());
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
        } catch (PostNotFoundException e) {
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUNT_POST.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.NOT_FOUNT_POST));
        }
    }
    
    @GetMapping("/all/{category}/speak")
    public ResponseEntity<Object> getAllNoticeSpeak(@PathVariable String category) {
        try {
            List<NoticeResponseDTO> noticeResponseDTOList = boardService.getAllNoticeSpeak(category);
            return ResponseEntity
                    .status(ResponseCode.SUCCESS_NOTICE_RETRIEVE.getStatus().value())
                    .body(new ResponseDTO<>(ResponseCode.SUCCESS_NOTICE_RETRIEVE, noticeResponseDTOList));
        } catch (PostNotFoundException e) {
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUNT_POST.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.NOT_FOUNT_POST));
        } catch (BoardIdNotFountException e) {
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUND_CATEGORY.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.NOT_FOUND_CATEGORY));
        } catch (EmptyPostException e) {
            return ResponseEntity
                    .status(ErrorCode.POST_EMPTY.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.POST_EMPTY));
        }
    }

    @GetMapping("all/{category}/normal")
    public ResponseEntity<Object> getAllNotice(@PathVariable String category,
                                               @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        try {
            Page<NoticeResponseDTO> noticeResponseDTOList = boardService.getAllNotice(category, page, size);
            return ResponseEntity
                    .status(ResponseCode.SUCCESS_NOTICE_RETRIEVE.getStatus().value())
                    .body(new ResponseDTO<>(ResponseCode.SUCCESS_NOTICE_RETRIEVE, noticeResponseDTOList));
        } catch (PostNotFoundException e) {
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUNT_POST.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.NOT_FOUNT_POST));
        } catch (BoardIdNotFountException e) {
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUND_CATEGORY.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.NOT_FOUND_CATEGORY));
        } catch (EmptyPostException e) {
            return ResponseEntity
                    .status(ErrorCode.POST_EMPTY.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.POST_EMPTY));
        } catch (WrongPaginationException e) {
            return ResponseEntity
                    .status(ErrorCode.WRONG_PAGINATION.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.WRONG_PAGINATION, e.getMessage()));
        }
    }
}
