package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReplyDTO {
    private Long id;
    private Long grp;
    private String contents;
    private Long boardId;
    private LocalDateTime createDT;
    private String writer;
    private Boolean commentPermission;

    public static GetReplyDTO mapToReplyDTO(Comment comment, Boolean permission) {
        return GetReplyDTO.builder()
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
