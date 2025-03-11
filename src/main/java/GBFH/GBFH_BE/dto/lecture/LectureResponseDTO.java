package GBFH.GBFH_BE.dto.lecture;

import GBFH.GBFH_BE.entity.Lecture;
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

    private String imgUrl;

    private Character regIng;
    private Integer quota; //정원

    public static LectureResponseDTO toDto(Lecture lecture) {

        String htmlContent = lecture.getContents();  // 기존 HTML 콘텐츠
        String img = null;
        StringBuilder contentBuilder = new StringBuilder();

        Document doc = Jsoup.parse(htmlContent);

        // 이미지 src 값 추출 및 https로 변환
        Elements imgTags = doc.select("img");
        if (!imgTags.isEmpty()) {
            Element firstImg = imgTags.get(0);
            String imgSrc = firstImg.attr("src");

            if (!imgSrc.contains("https")) {
                imgSrc = imgSrc.replace("http://", "https://");
                firstImg.attr("src", imgSrc);
            }

            img = imgSrc; // 리턴할 DTO에 담기 위해 저장
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
                .imgUrl(img)
                .quota(lecture.getQuota())
                .build();
    }
}
