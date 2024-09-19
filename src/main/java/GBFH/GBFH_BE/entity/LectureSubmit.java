package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_lecture_submit")
@IdClass(LectureSubmitPk.class) // 복합키 클래스
public class LectureSubmit {
    @Id
    @Column(name = "IDX", length = 20)
    private String idx;

    @Id
    @Column(name = "REGI_NO", length = 20)
    private String regiNo;
}
