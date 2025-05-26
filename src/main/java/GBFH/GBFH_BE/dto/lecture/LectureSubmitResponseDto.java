package GBFH.GBFH_BE.dto.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LectureSubmitResponseDto {
    private String id;
    private String title;
    private String nameKor;
    private String status;
}
