package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_board")
@Getter
public class Board {
    // 관리 번호
    @Column(name = "IDX", columnDefinition = "BIGINT")
    @Id
    private Long IDX;

    // 원본 글 구릅 관리 번호
    @Column(name = "GPP")
    private Long grp;

    // 순번
    @Column(name = "SEQ")
    private Long seq;

    // 답글 단계
    @Column(name = "LVL")
    private Long lvl;

    // 부모글 관리 번호
    @Column(name = "UP_IDX")
    private Long upIdx;

    // 공지 여부 -> 디폴트 0 -> 추후 사용할 거라면 넣고 아니면 빼자
    @Column(name = "NOTI")
    private Long noti;

    // 공지 시작 날짜
    @Column(name = "NOTI_START")
    private String notiStart;

    // 공지 해제 날짜
    @Column(name = "NOTI_END")
    private String notiEnd;

    // 대표(0: 입ㄹ잔, 1: 대표)
    @Column(name = "BEST")
    private Long best;

    // 정렬 번호
    @Column(name = "SORT_NO")
    private Long sortNo;

    // 게시판 관리번호
    @Column(name="BOARD_ID")
    private String boardId;

    // 제목
    @Column(name="TITLES")
    private String title;

    // 내용
    @Lob
    @Column(name = "CONTENTS", columnDefinition = "TEXT")
    private String contents;

    // 작성자명
    @Column(name = "WRITER")
    private String writer;

    // 마스킹 처리된 작성자 (이규*)
    @Column(name="MASK_WRITER")
    private String maskWriter; // 공지사항에서는 masking을 안 하긴 하는데...

    // 조회수 - default 0
    @Column(name = "READ", columnDefinition = "BIGINT")
    private Long read; //조회수

    // 사용:팝업 char(1) 형태로 저장
    @Column(name = "POPUP_YN", length = 1)
    private Character popupYN;

    // 중간에 팝업 상단, 좌측, 팝업 너비, 높이 필드 추가하지 않음

//    // 팝업 게시시작일
//    @Column(name = "POPUP_POSTED")
//    private String popupPosted;
//
//    // 팝업 게시 종료일
//    @Column(name = "POPUP_EXPIRED")
//    private String popupExpired;

    // 비밀 여부
    @Column(name = "SECRETE_YN", length = 1)
    private Character secreteYN;

    // 임시 삭제 여부
    @Column(name = "TRASH_YN", length = 1)
    private Character trashYN;

    // 에디터 여부
    @Column(name = "EDITOR_YN", length = 1)
    private Character editorYN;

    // 상태 (접수 > 처리 중 > 처리 완료)
    @Column(name = "STATUS")
    private String status; // 값이 있으면 접수라고 되어있음

    // 동
    @Column(name = "HOUSE_YN")
    private String houseYN;

    // 호실
    @Column(name = "ROOM_NO")
    private String roomNo;

    // 파일 관리 번호
    @Column(name = "FILE_ID")
    private Long fileId;

    // 링크
    @Column(name = "LINK_URL")
    private String linkUrl;

    // 등록자 권한
    @Column(name = "CREATE_LEVEL")
    private String createLevel;

    // 등록자
    @Column(name = "CREATE_ID")
    private String createId;

    // 등록일자
    @Column(name="CREATE_DT")
    private LocalDateTime createDT; //작성일 - 시간은 안 보여주더라

    // 등록자 아이피
    @Column(name = "CREATE_IP")
    private String createIP;

    // 수정자
    @Column(name = "UPDATE_ID")
    private String updateID;

    // 등록일자
    @Column(name="UPDATE_DT")
    private LocalDateTime updateDT; //작성일 - 시간은 안 보여주더라

    // 수정자 아이피
    @Column(name = "UPDATE_IP")
    private String updateIP;

    public void readNotice() {
        this.read = this.getRead() + 1;
    }

}
