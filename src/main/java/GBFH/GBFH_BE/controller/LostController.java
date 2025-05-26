package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.lost.*;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.service.CommentService;
import GBFH.GBFH_BE.service.LostService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static GBFH.GBFH_BE.util.NetworkUtils.getClientIP;

@RestController
@RequestMapping("/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;
    private final CommentService commentService;

    // 분실물 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> createLost(
            @Valid @RequestPart("createLostDTO") CreateLostDTO createLostDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpServletRequest request) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String clientIp = getClientIP(request);

        CreateLostDTO.Res res = lostService.createLost(createLostDTO, username, clientIp, files);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_LOST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_LOST, res));
    }

    // 분실물 전체 조회
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<?>> getAllLosts() {
        List<GetLostDTO.LIST> res = lostService.getAllLosts();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST, res));
    }

    // 분실물 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseDTO<?>> getDetailLosts(@PathVariable("id") Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        GetLostDTO.DETAIL res = lostService.getDetailLost(id, username);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST, res));
    }

    // 분실물 상태 별로 필터링
    @GetMapping
   public ResponseEntity<ResponseDTO<?>> getLostsByStatus(@PathParam("status") String status) {
        List<GetLostDTO.CategoryList> res = lostService.getLostsByStatus(status);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST, res));
    }

    // 분실물 댓글 작성
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> createComment(
            @Valid @RequestBody CreateCommentDTO createCommentDTO,
            @PathVariable("id") Long id,
            HttpServletRequest request ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String clientIp = getClientIP(request);

        CreateCommentDTO.Res res = commentService.createComment(id, username, createCommentDTO, clientIp);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_LOST_COMMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_LOST_COMMENT, res));
    }

    // 대댓글 작성
    @PostMapping("/{boardId}/reply/{commentId}") // 댓글의 id를 넣음
    public ResponseEntity<ResponseDTO<?>> createCommentReply(
            @Valid @RequestBody CreateCommentDTO createCommentDTO,
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId,
            HttpServletRequest request ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String clientIp = getClientIP(request);

        CreateCommentDTO.Res res = commentService.createReply(boardId, commentId, username, createCommentDTO, clientIp);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_LOST_COMMENT_REPLY.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_LOST_COMMENT_REPLY, res));
    }

    // 대댓글 및 댓글 삭제
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<ResponseDTO<?>> deleteCommentReply(
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        commentService.deleteCommentReply(boardId, commentId, username);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_LOST_COMMENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_LOST_COMMENT, null));
    }

    // 분실물 글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDTO<?>> deleteCommentReply(
            @PathVariable("boardId") Long boardId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        lostService.deleteLost(boardId, username);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_LOST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_LOST, null));
    }

    // 분실물 상태 수정
    @PutMapping("/status/{boardId}")
    public ResponseEntity<ResponseDTO<?>> updateLost(
            @PathVariable("boardId") Long boardId,
            @RequestBody UpdateLostStatusDTO updateLostStatusDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UpdateLostStatusDTO.Res res = lostService.updateLost(boardId, username, updateLostStatusDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_LOST_STATUS.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_LOST_STATUS, res));
    }

    // 분실물 글, 본문, 이미지 수정
    @PutMapping(path = "/content/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateLostContent(
            @PathVariable("boardId") Long boardId,
            @RequestPart("updateLostContentDTO") UpdateLostContentDTO updateLostContentDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "deleteFiles", required = false) List<String> deleteFiles,
            HttpServletRequest request ) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String clientIp = getClientIP(request);

        UpdateLostContentDTO.Res res = lostService.updateLostContent(boardId, username, updateLostContentDTO, files, deleteFiles, clientIp);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_LOST_CONTENT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_UPDATE_LOST_CONTENT, res));
    }

    // 분실물 검색
    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<?>> getAllBySearch(@RequestParam("q") String q) {
        List<GetLostDTO.CategoryList> res = lostService.getAllBySearch(q);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST, res));
    }
}
