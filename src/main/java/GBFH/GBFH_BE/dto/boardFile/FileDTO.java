package GBFH.GBFH_BE.dto.boardFile;


import GBFH.GBFH_BE.entity.BoardFile;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FileDTO {
    private Long id;
    private String uri; // 현재는 파일 이름만 넘겨주는데 나중에 연동할 때는 앞에 uri 붙여줄게여

    public static FileDTO toDTO(BoardFile boardFile) {
        return FileDTO.builder()
                .id(boardFile.getIdx())
                .uri(boardFile.getFileId())
                .build();
    }
}