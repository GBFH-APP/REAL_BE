package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UpdateLostContentDTO {
    private String title;
    private String contents;
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
        private List<FileDTO> files;

        public static Res mapToDTO(Board board, List<FileDTO> files) {
            return Res.builder()
                    .id(board.getIdx().toString())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .boardId(board.getBoardId().name())
                    .status(board.getStatus())
                    .createDT(board.getCreateDT())
                    .writer(board.getMaskWriter())
                    .files(files)
                    .build();
        }
    }
}
