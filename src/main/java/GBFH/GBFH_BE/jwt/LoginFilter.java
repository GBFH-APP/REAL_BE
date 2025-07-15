package GBFH.GBFH_BE.jwt;

import GBFH.GBFH_BE.code.ErrorCode;
import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.applicant.ApplicantDTO;
import GBFH.GBFH_BE.dto.response.ErrorResponseDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.entity.main.ApplicantSummary;
import GBFH.GBFH_BE.entity.main.Refresh;
import GBFH.GBFH_BE.exception.IdOrPasswordUnmatchException;
import GBFH.GBFH_BE.repository.main.ApplicantRepository;
import GBFH.GBFH_BE.repository.main.RefreshRedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Base64;


@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // 원래 얘가 로그인 처리 진행해줬음
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ApplicantRepository applicantRepository;
    private final RefreshRedisRepository refreshRedisRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        ApplicantSummary applicant = applicantRepository.findSummaryByLoginId(username)
                .orElseThrow(() -> new AuthenticationServiceException("사용자를 찾을 수 없습니다."));

        // 비밀번호를 Base64로 인코딩하여 비교
        if(isPasswordValid(password, applicant.getLoginPwd())) {
            // 비밀번호가 일치할 경우 JWT 토큰 생성
            String accessToken = jwtUtil.createJwt("accessToken", username, 300000L);
            String refreshToken = jwtUtil.createJwt("refreshToken", username,  1209600000L);

            Refresh refreshEntity = new Refresh(refreshToken, username);
            refreshRedisRepository.save(refreshEntity);

            // 응답에 토큰을 추가하여 반환
            response.setHeader("accessToken", "Bearer " + accessToken);
            response.setHeader("refreshToken", "Bearer " + refreshToken);

            ApplicantDTO.Res responseApplicant = ApplicantDTO.Res.mapToResLog(applicant);

            ResponseDTO responseDTO = new ResponseDTO<>(ResponseCode.SUCCESS_LOGIN, responseApplicant);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = null;

            try {
                jsonResponse = objectMapper.writeValueAsString(responseDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            try {
                response.getWriter().write(jsonResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // 비밀번호 불일치 예외
            response.setStatus(401);

            ErrorResponseDTO responseDTO = new ErrorResponseDTO(ErrorCode.ID_OR_PASSWARD_UNMATCH);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonResponse = null;
            try {
                jsonResponse = objectMapper.writeValueAsString(responseDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            try {
                response.getWriter().write(jsonResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    // 원래 이 부분이 로그인 성공 시 처리인데, 위에 비밀번호 비교 후 성공하면 토큰을 생성하고 헤더에 넣음 -> 로그인 성공 시 로직을 대체해서 일단 비우고 주석처리해둠
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
//    }


    // 로그인 실패 시 처리
    // 아이디가 없을 경우
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        response.setStatus(401);

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(ErrorCode.ID_OR_PASSWARD_UNMATCH);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(jsonResponse);
    }

    public static boolean isPasswordValid(String rawPassword, String encPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String decodedPassword;

        if (!StringUtils.isEmpty(rawPassword)) {
            if (!StringUtils.isEmpty(encPassword)) {
                // Base64 디코딩 처리
                byte[] decodedBytes = Base64.getDecoder().decode(encPassword);
                    decodedPassword = new String(decodedBytes);
                    return passwordEncoder.matches(rawPassword, decodedPassword);
            } else {
                return false;
            }
        }
        return false;
    }

}