package GBFH.GBFH_BE.dto.board;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardSummary;
import com.amazonaws.retry.v2.SimpleRetryPolicy;
import lombok.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class NoticeResponseDTO {
    private final String id; //idx
    private final String title;
    private final String content; //contents
    private final String imgUrlIOS; // 이미지는 필드로 final 선언
    private final String imgUrlAndroid;
    private final Integer height;
    private final Integer width;
    private final String writer;
    private final Long read;
    private final LocalDate createAt; //CREATE_DT
    private final List<FileDTO> fileList; // 첨부파일은 List<FileResponseDTO>로 설정
    // 이전글
    private SimplePostDTO previous;
    private SimplePostDTO next;

    // 다음글





    public static NoticeResponseDTO toDTO(Board notice, List<FileDTO> fileList, SimplePostDTO previous, SimplePostDTO next) {
        String htmlContent = notice.getContent();
        String imgAndroid = null;
        String imgIOS = null;
        String imgHeight = null;
        String imgWidth = null;
        StringBuilder contentBuilder = new StringBuilder();

        Document doc = Jsoup.parse(htmlContent);
        Elements imgTags = doc.select("img");

        if (!imgTags.isEmpty()) {
            Element firstImg = imgTags.get(0);
            String rawSrc = firstImg.attr("src");
            imgHeight = firstImg.attr("height");
            imgWidth = firstImg.attr("width");

            // iOS용: https로 강제
            imgIOS = rawSrc.replace("http", "https");

            // Android용: https 제거, :443 제거 (실제로 http 사용)
            imgAndroid = rawSrc.replace("https", "http").replace(":443", "");

            // HTML 내에서는 iOS 기준으로 수정
            firstImg.attr("src", imgIOS);
        }

        Elements pTags = doc.select("p");
        for (Element p : pTags) {
            contentBuilder.append(p.text()).append("\n");
        }

        return NoticeResponseDTO.builder()
                .id(notice.getIdx().toString())
                .title(notice.getTitle())
                .writer(notice.getWriter())
                .read(notice.getRead())
                .createAt(notice.getCreateDT().toLocalDate())
                .imgUrlIOS(imgIOS)
                .imgUrlAndroid(imgAndroid)
                .width(safeParseInt(imgWidth))
                .height(safeParseInt(imgHeight))
                .content(contentBuilder.toString())
                .fileList(fileList)
                .previous(previous)
                .next(next)
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