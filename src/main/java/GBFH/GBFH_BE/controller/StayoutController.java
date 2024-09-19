package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.applicant.CustomUserDetails;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.dto.stayout.StayoutResponseDTO;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.service.StayoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stayout")
public class StayoutController {
    private final StayoutService stayoutService;

    @GetMapping("/all")
    //내 거 전체 가져옴
    public ResponseEntity<ResponseDTO<?>> getAllStayout(@Valid @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<StayoutResponseDTO> stayoutResponseDTOS = stayoutService.getAllStayout(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_STAYOUT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_STAYOUT, stayoutResponseDTOS));
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDTO<?>> createStayOut(@Valid @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        StayoutResponseDTO stayoutResponseDTO = stayoutService.createStayOut(customUserDetails.getUsername());
    }
}
