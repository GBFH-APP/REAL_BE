package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Column(name = "C_IDX", columnDefinition = "BIGINT")
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // 원본 글 구릅 관리 번호
    @Column(name = "C_GRP") // 임시로 해둠
    @Setter
    private Long grp;

    // 순번
    @Column(name = "C_SEQ")
    private Long seq;

    // 답글 단계
    @Column(name = "C_LVL")
    private Long lvl;

    // 부모글 관리 번호 -> 여기에 BoardIdx 저장해둠
    @Column(name = "C_UP_IDX")
    private Long upIdx;

    // 본문
    @Column(name = "C_CONTENTS")
    private String contents;

    // 삭제 여부
    @Column(name = "C_DEL_YN")
    private String delYN;

    // 사용자 마스크
    @Column(name = "C_MASK_WRITER")
    private String maskWriter;

    // 등록자 권한
    @Column(name = "C_CREATE_LEVEL")
    private String createLevel;

    // 등록자
    @Column(name = "C_CREATE_ID", length = 60)
    private String createId;

    // 등록일자
    @Column(name="C_CREATE_DT")
    private LocalDateTime createDT; //작성일 - 시간은 안 보여주더라

    // 등록자 아이피
    @Column(name = "C_CREATE_IP", length = 40)
    private String createIP;
}
