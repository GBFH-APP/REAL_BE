package GBFH.GBFH_BE.dto.lost;


import GBFH.GBFH_BE.entity.Comment;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentDTO {
    @NotEmpty(message = "본문은 필수 입력 값입니다.")
    private String contents;

    public static Comment mapToComment(CreateCommentDTO createCommentDTO, Long boardIdx, Long grp, String username, String userNo, String clientIp) {
        return Comment.builder()
                .grp(grp)
                .seq(1L) // 대부분 1로 저장됨
                .lvl(1L)
                .upIdx(boardIdx)
                .contents(createCommentDTO.getContents())
                .maskWriter(createCommentDTO.makeWriterMask(username))
                .delYN("N")
                .createLevel("dorm")
                .createId(userNo)
                .createIP(clientIp)
                .createDT(LocalDateTime.now())
                .build();
    }

    public static Comment mapToCommentReply(CreateCommentDTO createCommentDTO, Long boardIdx, Long grp, String username, String userNo, String clientIp) {
        return Comment.builder()
                .grp(grp)
                .seq(1L) // 이거 순서대로 가게 해?
                .lvl(2L) // 2단계로 감
                .upIdx(boardIdx)
                .contents(createCommentDTO.getContents())
                .maskWriter(createCommentDTO.makeWriterMask(username))
                .delYN("N")
                .createLevel("dorm")
                .createId(userNo)
                .createIP(clientIp)
                .createDT(LocalDateTime.now())
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        private String id;
        private Long grp;
        private Long lvl;
        private String contents;
        private Long boardId;
        private LocalDateTime createDT;

        public static CreateCommentDTO.Res mapToDTO(Comment comment) {
            return Res.builder()
                    .id(comment.getIdx().toString())
                    .grp(comment.getGrp())
                    .lvl(comment.getLvl())
                    .contents(comment.getContents())
                    .boardId(comment.getUpIdx())
                    .createDT(comment.getCreateDT())
                    .build();
        }
    }

    // 유저 이름 마스킹
    private String makeWriterMask(String username) {
        if (username == null || username.length() < 2)
            return username;

        if (username.length() == 2)
            return username.substring(0, 1) + "*";

        return username.substring(0, username.length() - 1) + "*";
    }
}
