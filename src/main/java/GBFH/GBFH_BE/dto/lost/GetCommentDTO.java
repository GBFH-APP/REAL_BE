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
public class GetCommentDTO {
    private String commentId;
    private Long grp;
    private String contents;
    private Long boardId;
    private LocalDateTime createDT;
    private String writer;
    private Boolean commentPermission;
    private List<GetReplyDTO> replies;

    public static GetCommentDTO mapToCommentDTO(Comment comment, Boolean permission, List<GetReplyDTO> replies) {
        return GetCommentDTO.builder()
                .commentId(comment.getIdx().toString())
                .grp(comment.getGrp())
                .contents(comment.getContents())
                .boardId(comment.getUpIdx())
                .createDT(comment.getCreateDT())
                .writer(comment.getMaskWriter())
                .commentPermission(permission)
                .replies(replies)
                .build();
    }
}
