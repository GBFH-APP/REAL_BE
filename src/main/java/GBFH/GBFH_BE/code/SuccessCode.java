package GBFH.GBFH_BE.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SuccessCode {
    /**
     * User
     */
    SUCCESS_REGISTER(HttpStatus.CREATED, "회원가입을 성공했습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다. 헤더 토큰을 확인하세요."),
    SUCCESS_RETRIEVE_USER(HttpStatus.OK, "유저 정보를 성공적으로 조회했습니다."),
    SUCCESS_REISSUE(HttpStatus.OK, "토큰 재발급을 성공했습니다. 헤더 토큰을 확인하세요."),
    SUCCESS_UPDATE_USER(HttpStatus.OK, "유저 정보를 성공적으로 수정했습니다."),
    SUCCESS_RETRIEVE_ALL_USERS(HttpStatus.OK, "모든 사용자를 성공적으로 조회했습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "성공적으로 로그아웃했습니다."),
    SUCCESS_DELETE_USER(HttpStatus.OK, "유저가 성공적으로 삭제되었습니다."),

    /**
     *  PASS
     * */
    SUCESS_COME_OUT(HttpStatus.OK, "출입이 성공적으로 기록되었습니다."),


    /*
     * notice
     */
    SUCCESS_NOTICE_REGISTER(HttpStatus.CREATED, "공지사항을 작성했습니다."),
    SUCCESS_NOTICE_RETRIEVE(HttpStatus.OK, "공지사항을 성공적으로 조회했습니다."),
    SUCCESS_NOTICE_RETRIEVE_ALL(HttpStatus.OK, "모든 공지사항을 성공적으로 조회했습니다."),
    SUCCESS_NOTICE_DELETE(HttpStatus.OK, "공지사항이 성공적으로 삭제되었습니다."),
    SUCCESS_NOTICE_DELETE_IMAGE(HttpStatus.OK, "이미지가 성공적으로 삭제되었습니다."),
    SUCCESS_NOTICE_UPDATE(HttpStatus.OK, "공지사항이 성공적으로 수정되었습니다."),


    /*
     * SleepOver
     * */
    SUCCESS_SLEEPOVER_REGISTER(HttpStatus.CREATED, "외박 신청을 성공했습니다."),
    SUCCESS_SLEEPOVER_RETRIEVE(HttpStatus.OK, "외박 신청을 성공적으로 조회했습니다."),
    SUCCESS_SLEEPOVER_RETRIEVE_ALL(HttpStatus.OK, "모든 외박 신청을 성공적으로 조회했습니다."),

    /**
     * Lost
     */
    SUCCESS_CREATE_LOST(HttpStatus.CREATED, "분실물 글을 성공적으로 작성했습니다"),
    SUCCESS_RETRIEVE_LOST_LIST(HttpStatus.OK, "분실물 글을 성공적으로 조회했습니다."),
    SUCCESS_RETRIEVE_LOST(HttpStatus.OK, "분실물 상세정보를 성공적으로 조회했습니다."),
    SUCCESS_UPDATE_LOST_STATUS(HttpStatus.OK, "분실물 상태를 성공적으로 변경했습니다."),
    SUCCESS_CREATE_COMMENT(HttpStatus.CREATED, "댓글을 성공적으로 작성했습니다."),
    SUCCESS_CREATE_REPLY(HttpStatus.CREATED, "대댓글을 성공적으로 작성했습니다."),
    SUCCESS_UPDATE_LOST_CONTENT(HttpStatus.OK, "분실물 글을 성공적으로 수정했습니다."),
    SUCCESS_DELETE_LOST(HttpStatus.OK, "분실물 글을 성공적으로 삭제했습니다."),

    ;


    private final HttpStatus status;
    private final String message;
}