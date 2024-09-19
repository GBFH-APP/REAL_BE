package GBFH.GBFH_BE.dto.lecture;

import GBFH.GBFH_BE.entity.Lecture;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LectureResponseDTO {
    private String idx;
    private String year;

    private String title;
    private String contents;

    private LocalDate startDT;
    private LocalDate endDT;

    private String time;
    private String place;

    private String regMethod;
    private String regStartDT; //시작 연월일
    private String regEndDT;

    private Character regIng;
    private Integer quota; //정원

    public static LectureResponseDTO toDto(Lecture lecture) {
        return LectureResponseDTO.builder()
                .idx(lecture.getIdx())
                .year(lecture.getYear())
                .title(lecture.getTitle())
                .contents(lecture.getContents())
                .startDT(lecture.getStartDt())
                .endDT(lecture.getEndDt())
                .time(lecture.getTime())
                .place(lecture.getPlace())
                .regMethod(lecture.getRegMethod())
                .regStartDT(lecture.getRegStartDt() + " " + lecture.getRegStartHour() + ":" + lecture.getRegStartMinute())
                .regEndDT(lecture.getRegEndDt() + " " + lecture.getRegEndHour() + ":" + lecture.getRegEndMinute())
                .regIng(lecture.getRegIng())
                .quota(lecture.getQuota())
                .build();
    }
}
