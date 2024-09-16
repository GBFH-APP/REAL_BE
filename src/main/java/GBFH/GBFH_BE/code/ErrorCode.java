package GBFH.GBFH_BE.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_LOST_POST(HttpStatus.BAD_REQUEST, "분실물 글이 아닙니다."),


    /**
     * 401 UNAUTHORIZED: 토큰 만료
     */
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "요청 헤더에 토큰이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),

    /**
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    /**
     * 406
     * **/
    NOT_NULL_TITLE(HttpStatus.NOT_ACCEPTABLE, "제목 없이 저장할 수 없습니다."),
    NOT_NULL(HttpStatus.NOT_ACCEPTABLE, "필수 입력 사항을 확인해주세요."),
    UNAVAILABLE_VALUE(HttpStatus.NOT_ACCEPTABLE, "사용할 수 없는 값입니다."),
    NOT_FOUNT_POST(HttpStatus.NOT_ACCEPTABLE, "해당 본문을 찾을 수 없습니다."),
    POST_EMPTY(HttpStatus.NOT_ACCEPTABLE, "글이 비었습니다."),
    DUPLICATE_DATE(HttpStatus.NOT_ACCEPTABLE, "중복된 날짜로 생성할 수 없습니다."),
    MULTIPART_ERROR(HttpStatus.NOT_ACCEPTABLE, "multipart/form-data가 존재하지 않습니다."),
    NOT_FOUND_IMAGE(HttpStatus.NOT_ACCEPTABLE, "해당 이미지가 존재하지 않습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_ACCEPTABLE, "해당 카테고리가 존재하지 않습니다."),

    /**
     * 502
     */

    UNABLE_TO_RESOLVE_HOST(HttpStatus.BAD_GATEWAY, "호스트를 찾을 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}