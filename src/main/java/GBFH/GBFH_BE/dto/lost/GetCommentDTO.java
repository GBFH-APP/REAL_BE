package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentDTO {
    private Long id;
    private Long grp;
    private String contents;
    private Long boardId;
    private String createId;
    private String createIP;
    private LocalDateTime createDT;
    private String writer;

    public static GetCommentDTO mapToDTO(Comment comment) {
        return GetCommentDTO.builder()
                .id(comment.getIdx())
                .grp(comment.getGrp())
                .contents(comment.getContents())
                .boardId(comment.getUpIdx())
                .createId(comment.getCreateId())
                .createIP(comment.getCreateIP())
                .createDT(comment.getCreateDT())
                .writer(comment.getMaskWriter())
                .build();
    }
}
