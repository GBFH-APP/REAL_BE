package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ErrorCode;
import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.applicant.ApplicantDTO;
import GBFH.GBFH_BE.dto.applicant.UpdateApplicantDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.entity.main.Refresh;
import GBFH.GBFH_BE.jwt.JWTUtil;
import GBFH.GBFH_BE.repository.main.RefreshRedisRepository;
import GBFH.GBFH_BE.service.ApplicantService;
import GBFH.GBFH_BE.util.TokenErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/applicant")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicantService applicantService;
    private final JWTUtil jwtUtil;
    private final RefreshRedisRepository refreshRedisRepository;

    @GetMapping()
    public ResponseEntity<ResponseDTO<?>> getUserList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        ApplicantDTO.DetailRes response = applicantService.getApplicant(username);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_USER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_USER, response));
    }

    @PatchMapping()
    public ResponseEntity<ResponseDTO<?>> updateUser(@RequestBody UpdateApplicantDTO.Req updateApplicantDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UpdateApplicantDTO.Res response = applicantService.updateApplicate(username, updateApplicantDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_USER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_USER, response));
    }

    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 헤더에서 refresh키에 담긴 토큰을 꺼냄
        String refreshToken = request.getHeader("refresh");

        if (refreshToken == null) {
            TokenErrorResponse.sendErrorResponse(response, ErrorCode.TOKEN_MISSING);
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            TokenErrorResponse.sendErrorResponse(response, ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        String type = jwtUtil.getType(refreshToken);
        if (!type.equals("refreshToken")) {
            TokenErrorResponse.sendErrorResponse(response, ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Optional<Refresh> isExist = refreshRedisRepository.findById(refreshToken);
        if (isExist.isEmpty()) {
            TokenErrorResponse.sendErrorResponse(response, ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        String username = jwtUtil.getUsername(refreshToken);

        // 새로운 Access token과 refreshToken 생성
        String newAccessToken = jwtUtil.createJwt("accessToken", username, 300000L);
        String newRefreshToken = jwtUtil.createJwt("refreshToken", username,  1_209_600_000L);

        response.setHeader("accessToken", "Bearer " + newAccessToken);
        response.setHeader("refreshToken", "Bearer " + newRefreshToken);

        refreshRedisRepository.deleteById(refreshToken);
        Refresh refreshEntity = new Refresh(newRefreshToken, username);
        refreshRedisRepository.save(refreshEntity);

        return "Refresh Token 재발급 완료. 헤더를 확인하세요";
    }
}
