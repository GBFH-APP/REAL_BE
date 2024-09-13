package GBFH.GBFH_BE.dto.boardconfig;

import GBFH.GBFH_BE.entity.BoardConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardConfigDTO {
    private String boardId;
    private String boardName;

    public static BoardConfigDTO mapEntityToDTO(BoardConfig boardConfig) {
        return BoardConfigDTO.builder()
                .boardId(boardConfig.getBoardId())
                .boardName(boardConfig.getBoardName())
                .build();
    }
}
