package GBFH.GBFH_BE.dto.boardFile;


import GBFH.GBFH_BE.entity.BoardFile;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FileResponseDTO {
    private Long boardId; // 어떤 글에 해당하는 첨부파일인지
    private List<FileDTO> fileDTOList;

    @Getter
    @Builder
    public static class FileDTO {
        private Long id;
        private String uri; // 현재는 파일 이름만 넘겨주는데 나중에 연동할 때는 앞에 uri 붙여줄게여
    }

    public static FileDTO toDTO(BoardFile boardFile){
        return FileDTO.builder()
                .id(boardFile.getIDX())
                .uri(boardFile.getFILE_ID())
                .build();
    }

    public static FileResponseDTO toDTOList(List<BoardFile> boardFiles) {
        return FileResponseDTO.builder()
                .fileDTOList(boardFiles.stream().map(FileResponseDTO::toDTO).collect(Collectors.toList()))
                .build();
    }
}
