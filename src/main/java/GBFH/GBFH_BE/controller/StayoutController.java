package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.applicant.CustomUserDetails;
import GBFH.GBFH_BE.dto.board.NoticeResponseDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.dto.stayout.StayoutRequestDTO;
import GBFH.GBFH_BE.dto.stayout.StayoutResponseDTO;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.service.PaginateService;
import GBFH.GBFH_BE.service.StayoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static GBFH.GBFH_BE.util.NetworkUtils.getClientIP;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stayout")
public class StayoutController {
    private final StayoutService stayoutService;

    @GetMapping("/all")
    //내 거 전체 가져옴
    public ResponseEntity<ResponseDTO<?>> getAllStayout(@Valid @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<StayoutResponseDTO> stayoutResponseDTOS = stayoutService.getAllStayout(customUserDetails.getUsername(), page, size);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_STAYOUT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_STAYOUT, stayoutResponseDTOS));
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDTO<?>> createStayOut(@Valid @RequestBody StayoutRequestDTO stayoutRequestDTO,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        HttpServletRequest request) {
        String clientIp = getClientIP(request);
        StayoutResponseDTO stayoutResponseDTO = stayoutService.createStayOut(customUserDetails.getUsername(), stayoutRequestDTO, clientIp);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_STAYOUT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_STAYOUT, stayoutResponseDTO));
    }

//    @GetMapping("/")
//    public ResponseEntity<ResponseDTO<?>> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
//    }
}
