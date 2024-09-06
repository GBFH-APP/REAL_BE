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
    private Long IDX;

    @Column(name="BOARD_ID")
    private String BOARD_ID;

    @Column(name="TITLE")
    private String TITLE;

    @Lob
    @Column(name = "CONTENTS", columnDefinition = "TEXT")
    private String CONTENTS;

    @Column(name="MASK_WRITER")
    private String MASK_WRITER; // 공지사항에서는 masking을 안 하긴 하는데...

    @Column(name = "READ", columnDefinition = "BIGINT")
    private Long READ; //조회수

    @Column(name="CREATE_DT")
    private LocalDateTime CREATE_DT; //작성일 - 시간은 안 보여주더라


    public void readNotice() {
        this.READ = this.getREAD() + 1;
    }
}
