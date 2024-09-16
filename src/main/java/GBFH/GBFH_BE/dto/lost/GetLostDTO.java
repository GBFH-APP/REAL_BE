package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class GetLostDTO {
    // 리스트 조회
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LIST {
        private Long id;
        private String title;
        private String contents;
        private String status;
        private LocalDateTime createDT;
        // 이미지 추가해야 함

        public static LIST mapToDTO(Board board) {
            return LIST.builder()
                    .id(board.getIdx())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .status(board.getStatus())
                    .createDT(board.getCreateDT())
                    .build();
        }
    }

    // 상세 조회
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DETAIL {
        private Long id;

    }
}
