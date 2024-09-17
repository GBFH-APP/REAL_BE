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
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),

    /**
     * notice
     */
    SUCCESS_NOTICE_REGISTER(HttpStatus.CREATED, "공지사항을 작성했습니다."),
    SUCCESS_NOTICE_RETRIEVE(HttpStatus.OK, "공지사항을 성공적으로 조회했습니다."),
    SUCCESS_NOTICE_RETRIEVE_ALL(HttpStatus.OK, "모든 공지사항을 성공적으로 조회했습니다."),
    SUCCESS_NOTICE_DELETE(HttpStatus.OK, "공지사항이 성공적으로 삭제되었습니다."),
    SUCCESS_NOTICE_DELETE_IMAGE(HttpStatus.OK, "이미지가 성공적으로 삭제되었습니다."),
    SUCCESS_NOTICE_UPDATE(HttpStatus.OK, "공지사항이 성공적으로 수정되었습니다."),

    /**
     * board config
     */
    SUCCESS_RETRIEVE_BOARD_CONFIG(HttpStatus.OK, "게시글 리스트를 성공적으로 조회했습니다"),

    /**
     * lost
     */
    SUCCESS_CREATE_LOST(HttpStatus.CREATED, "분실물 글을 성공적으로 저장했습니다."),
    SUCCESS_RETRIEVE_LOST_LIST(HttpStatus.OK, "분실물 리스트를 성공적으로 조회했습니다."),
    SUCCESS_RETRIEVE_LOST_DETAIL(HttpStatus.OK, "분실물 상세 정보를 성공적으로 조회했습니다."),
    SUCCESS_CREATE_LOST_COMMENT(HttpStatus.CREATED, "분실물 댓글을 성공적으로 저장했습니다."),
    SUCCESS_CREATE_LOST_COMMENT_REPLY(HttpStatus.CREATED, "분실물 대댓글을 성공적으로 저장했습니다."),
    SUCCESS_DELETE_LOST_COMMENT(HttpStatus.OK, "분실물 댓글 및 대댓글을 성공적으로 삭제했습니다."),
    SUCCESS_DELETE_LOST(HttpStatus.OK, "분실물 글을 성공적으로 삭제했습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}