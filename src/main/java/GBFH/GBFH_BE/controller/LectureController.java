package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.applicant.CustomUserDetails;
import GBFH.GBFH_BE.dto.lecture.LectureResponseDTO;
import GBFH.GBFH_BE.dto.lecture.LectureSubmitResponseDto;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.service.LectureService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static GBFH.GBFH_BE.util.NetworkUtils.getClientIP;

@RestController
@RequestMapping("/lecture")
@Slf4j
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @GetMapping("/all/{yorn}")
    public ResponseEntity<ResponseDTO<?>> getAllLecture(@Valid @PathVariable char yorn,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<LectureResponseDTO> lectureResponseDTOS = lectureService.getAllLecture(yorn, page, size);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LECTURE_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LECTURE_LIST, lectureResponseDTOS));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseDTO<?>> getLectureDetail(@Valid @PathVariable String id) {
        LectureResponseDTO lectureResponseDTO = lectureService.getLectureDetail(id);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LECTURE_DETAIL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LECTURE_DETAIL, lectureResponseDTO));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> createLectureSubmit(@Valid @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @PathVariable String id,
                                                              HttpServletRequest request) {

        String clientIp = getClientIP(request);
        LectureSubmitResponseDto lectureSubmitResponseDto = lectureService.createLectureSubmit(customUserDetails.getUsername(), id, clientIp);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_LECTURE_SUBMIT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_LECTURE_SUBMIT, lectureSubmitResponseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteLectureSubmit(@Valid @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @PathVariable String id) {

        LectureSubmitResponseDto lectureSubmitResponseDto = lectureService.deleteLectureSubmit(customUserDetails.getUsername(), id);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_LECTURE_SUBMIT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_DELETE_LECTURE_SUBMIT, lectureSubmitResponseDto));
    }

    @GetMapping("/{month}")
    public ResponseEntity<ResponseDTO<?>> getThisMonthLectureList(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Integer month) {
        List<Integer> lectureMontlyList = lectureService.getLectureMontlyList(month);

        return  ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LECTURE_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LECTURE_LIST, lectureMontlyList));

    }


}
