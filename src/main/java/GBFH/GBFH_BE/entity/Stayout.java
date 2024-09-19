package GBFH.GBFH_BE.entity;

import GBFH.GBFH_BE.dto.stayout.StayoutRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_stayout")
@Getter
@IdClass(StayoutPk.class) // 복합키 클래스
public class Stayout {
    @Column(name = "REGISTRATION_NO", length = 20)
    @Id
    private String regiNo;

    @Column(name = "SEQ")
    @Id
    private Integer seq;

    @Column(name = "START_DT")
    private LocalDateTime startDT;

    @Column(name = "END_DT")
    private LocalDateTime endDT;

    @Column(name = "RETURN_DT", nullable = true)
    private LocalDateTime returnDT; //중도 복귀일
    //생성시에는 null 넣으면 됨

    @Lob
    @Column(name = "REASON", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "APPROVE_TYPE")
    private Character approveType;

    // 등록자
    @Column(name = "CREATE_ID", length = 60)
    private String createId;

    // 등록일자
    @Column(name="CREATE_DT")
    private LocalDateTime createDT; //작성일 - 시간은 안 보여주더라

    // 등록자 아이피
    @Column(name = "CREATE_IP", length = 40)
    private String createIP;

    // 수정자
    @Column(name = "UPDATE_ID", length = 60)
    private String updateID;

    // 등록일자
    @Column(name="UPDATE_DT")
    private LocalDateTime updateDT; //작성일 - 시간은 안 보여주더라

    // 수정자 아이피
    @Column(name = "UPDATE_IP", length = 40)
    private String updateIP;
}
