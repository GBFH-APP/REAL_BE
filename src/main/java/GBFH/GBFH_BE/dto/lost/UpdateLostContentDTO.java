package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.dto.boardFile.FileResponseDTO;
import GBFH.GBFH_BE.entity.Board;
import jakarta.validation.constraints.NotEmpty;
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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        private Long id;
        private String title;
        private String contents;
        private String boardId;
        private String status;
        private LocalDateTime createDT;
        private String writer;
        private List<FileResponseDTO.FileDTO> files;

        public static Res mapToDTO(Board board, List<FileResponseDTO.FileDTO> files) {
            return Res.builder()
                    .id(board.getIdx())
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
