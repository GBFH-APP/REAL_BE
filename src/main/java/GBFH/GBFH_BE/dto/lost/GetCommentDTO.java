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
    private LocalDateTime createDT;
    private String writer;
    private Boolean commentPermission;

    public static GetCommentDTO mapToDTO(Comment comment, Boolean permission) {
        return GetCommentDTO.builder()
                .id(comment.getIdx())
                .grp(comment.getGrp())
                .contents(comment.getContents())
                .boardId(comment.getUpIdx())
                .createDT(comment.getCreateDT())
                .writer(comment.getMaskWriter())
                .commentPermission(permission)
                .build();
    }
}
