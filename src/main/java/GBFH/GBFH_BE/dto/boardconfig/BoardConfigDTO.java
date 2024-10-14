package GBFH.GBFH_BE.dto.boardconfig;

import GBFH.GBFH_BE.entity.BoardConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardConfigDTO {
    private String boardId;
    private String boardName;

    public static BoardConfigDTO mapEntityToDTO(BoardConfig boardConfig) {
        return BoardConfigDTO.builder()
                .boardId(boardConfig.getBoardId().name())
                .boardName(boardConfig.getBoardName())
                .build();
    }

    private static final Map<String, String> EXTENSION_TO_MIME_MAP = new HashMap<>();

    static {
        EXTENSION_TO_MIME_MAP.put("png", "image/png");
        EXTENSION_TO_MIME_MAP.put("jpeg", "image/jpeg");
        EXTENSION_TO_MIME_MAP.put("jpg", "image/jpeg");
        // 필요에 따라 다른 확장자도 추가 가능
    }

    // 확장자를 기반으로 MIME 타입 반환
    public static String getMimeType(String extension) {
        return EXTENSION_TO_MIME_MAP.get(extension.toLowerCase());
    }
}
