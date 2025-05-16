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
    private final String imgUrl; // 이미지는 필드로 final 선언
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
        String htmlContent = notice.getContent();  // 기존 HTML 콘텐츠
        String img = null;
        String imgHeight = null;
        String imgWidth = null;
        StringBuilder contentBuilder = new StringBuilder();

        Document doc = Jsoup.parse(htmlContent);

        // 이미지 src 값 추출 및 https로 변환
        Elements imgTags = doc.select("img");
        if (!imgTags.isEmpty()) {
            Element firstImg = imgTags.get(0);
            String imgSrc = firstImg.attr("src");
            imgHeight = firstImg.attr("height");
            imgWidth = firstImg.attr("width");

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

        // 빌더 패턴을 사용하여 객체 생성
        return NoticeResponseDTO.builder()
                .id(notice.getIdx().toString())
                .title(notice.getTitle())
                .writer(notice.getWriter())
                .read(notice.getRead())
                .createAt(notice.getCreateDT().toLocalDate())
                .imgUrl(img)
                .width(Integer.parseInt(Objects.requireNonNull(imgWidth)))
                .height(Integer.parseInt(Objects.requireNonNull(imgHeight)))
                .content(contentBuilder.toString())
                .fileList(fileList)  // 첨부파일 설정
                .previous(previous)
                .next(next)
                .build();
    }
}