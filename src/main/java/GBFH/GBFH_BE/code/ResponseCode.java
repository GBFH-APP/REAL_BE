package GBFH.GBFH_BE.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {
    /**
     * User
     */
    SUCCESS_RETRIEVE_USER(HttpStatus.OK, "유저 목록 조회를 성공했습니다. (추후 삭제 필요)"),

    ;

    private final HttpStatus status;
    private final String message;
}