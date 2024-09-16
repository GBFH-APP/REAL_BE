package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_board")
@Getter
public class Board {
    @Column(name = "IDX", columnDefinition = "BIGINT")
    @Id
    private Long idx;

    @Column(name="BOARD_ID")
    private String boardId;

    @Column(name="TITLE")
    private String title;

    @Lob
    @Column(name = "CONTENTS", columnDefinition = "TEXT")
    private String contents;

    @Column(name="MASK_WRITER")
    private String maskWriter; // 공지사항에서는 masking을 안 하긴 하는데...

    @Column(name = "READ", columnDefinition = "BIGINT")
    private Long read; //조회수

    @Column(name="CREATE_DT")
    private LocalDateTime createDt; //작성일 - 시간은 안 보여주더라

    //공지사항의 공지
    @Column(name="NOTI", columnDefinition = "INT")
    private Integer noti; //0 일반 1: 공지

    @Column(name="NOTI_START")
    private String notiStart; //공지 시작일

    @Column(name="NOTI_END")
    private String notiEnd; //공지 시작일



    public Board readBoard() {
        this.read = this.getRead() + 1;
        return this;
    }
}
