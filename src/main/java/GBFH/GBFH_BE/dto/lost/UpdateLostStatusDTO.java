package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.entity.Board;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class UpdateLostStatusDTO {
    @NotNull(message = "상태는 필수 입력 값입니다.")
    private String status;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        private String id;
        private String title;
        private String contents;
        private String boardId;
        private String status;
        private LocalDateTime createDT;
        private String writer;
    }

    public static Res mapToDTO(Board board) {
        return Res.builder()
                .id(board.getIdx().toString())
                .title(board.getTitle())
                .contents(board.getContents())
                .boardId(board.getBoardId().name())
                .status(board.getStatus())
                .createDT(board.getCreateDT())
                .writer(board.getMaskWriter())
                .build();
    }
}
