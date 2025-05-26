package GBFH.GBFH_BE.dto.lecture;

import GBFH.GBFH_BE.entity.main.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    private LocalDate createAt; //CREATE_DT

    private LocalDate startDT;
    private LocalDate endDT;

    private String time;
    private String place;

    private String regMethod;
    private String regStartDT; //시작 연월일
    private String regEndDT;

    private String imgUrlIOS;
    private String imgUrlAndroid;

    private Integer width;
    private Integer height;

    private Character regIng;
    private Integer quota; //정원

    public static LectureResponseDTO toDto(Lecture lecture) {

        String htmlContent = lecture.getContents();
        String imgAndroid = null;
        String imgIOS = null;
        String imgHeight = null;
        String imgWidth = null;
        StringBuilder contentBuilder = new StringBuilder();

        Document doc = Jsoup.parse(htmlContent);
        Elements imgTags = doc.select("img");

        for (Element imgTag : imgTags) {
            String src = imgTag.attr("src");

            if (src != null && src.contains("editorData")) {
                imgHeight = imgTag.attr("height");
                imgWidth = imgTag.attr("width");

                imgIOS = src.replace("http://", "https://");
                imgAndroid = src.replace("https://", "http://").replace(":443", "");

                imgTag.attr("src", imgIOS); // 콘텐츠용 수정
                break; // 첫 유효한 이미지만 사용
            }
        }

        // <p> 태그의 텍스트만 추출
        Elements pTags = doc.select("p");
        for (Element p : pTags) {
            contentBuilder.append(p.text()).append("\n");
        }

        return LectureResponseDTO.builder()
                .idx(lecture.getIdx())
                .year(lecture.getYear())
                .title(lecture.getTitle())
                .createAt(lecture.getCreateDt().toLocalDate())
                .contents(lecture.getContents())
                .startDT(lecture.getStartDt())
                .endDT(lecture.getEndDt())
                .time(lecture.getTime())
                .place(lecture.getPlace())
                .regMethod(lecture.getRegMethod())
                .regStartDT(lecture.getRegStartDt() + " " + lecture.getRegStartHour() + ":" + lecture.getRegStartMinute())
                .regEndDT(lecture.getRegEndDt() + " " + lecture.getRegEndHour() + ":" + lecture.getRegEndMinute())
                .regIng(lecture.getRegIng())
                .imgUrlIOS(imgIOS)
                .imgUrlAndroid(imgAndroid)
                .width(safeParseInt(imgWidth))
                .height(safeParseInt(imgHeight))
                .quota(lecture.getQuota())
                .build();
    }

    private static Integer safeParseInt(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
