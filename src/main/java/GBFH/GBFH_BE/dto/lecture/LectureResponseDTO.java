package GBFH.GBFH_BE.dto.lecture;

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
    private Character regEnd;
    private Integer quota; //정원

}
