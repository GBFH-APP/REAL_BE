package GBFH.GBFH_BE.entity.main;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_lecture_submit")
@IdClass(LectureSubmitPk.class) // 복합키 클래스
public class LectureSubmit {
    @Id
    @Column(name = "IDX", length = 20) // 특강 키
    private String idx;

    @Id
    @Column(name = "REG_NO", length = 20)
    private String regiNo;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "CREATE_ID", length = 60)
    private String createId;

    // 등록일자
    @Column(name="CREATE_DT")
    private LocalDateTime createDT; //작성일 - 시간은 안 보여주더라

    // 등록자 아이피
    @Column(name = "CREATE_IP", length = 40)
    private String createIP;
}
