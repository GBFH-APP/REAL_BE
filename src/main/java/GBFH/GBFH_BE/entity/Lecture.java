package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "t_lecture")
@AllArgsConstructor
@NoArgsConstructor
public class Lecture {
    @Id
    @Column(name = "IDX", length = 20)
    private String idx;

    @Column(name = "YEAR_TERM", length = 4)
    private String year;

    @Column(name = "SUBJECT", length = 300)
    private String title;

    @Lob
    @Column(name = "CONTENTS", columnDefinition = "MEDIUMTEXT")
    private String contents;

    @Column(name = "RUN_START")
    private LocalDate startDt;

    @Column(name = "RUN_END")
    private LocalDate endDt;

    @Column(name = "RUN_TIME", length = 100)
    private String time;

    @Column(name = "RUN_PLACE", length = 100)
    private String place;

    @Column(name = "OPEN_YN")
    private Character open; // 거의 Y

    @Column(name = "REG_METHOD", length = 20)
    private String regMethod;

    @Column(name = "REG_START_YMD", length = 10)
    private String regStartDt; //시작 연월일

    @Column(name = "REG_START_HH", length = 2)
    private String regStartHour;

    @Column(name = "REG_START_MM", length = 2)
    private String regStartMinute;

    @Column(name = "REG_END_YMD", length = 10)
    private String regEndDt;

    @Column(name = "REG_END_HH", length = 2)
    private String regEndHour;

    @Column(name = "REG_END_MM", length = 2)
    private String regEndMinute;

    @Column(name = "REG_END_YN")
    private Character regIng;

    @Column(name = "QUOTA", length = 10)
    private Integer quota; //정원

    @Column(name = "CREATE_DT")
    private LocalDateTime createDt;

}
