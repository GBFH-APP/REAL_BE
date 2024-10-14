package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardconfig.BoardConfigDTO;
import GBFH.GBFH_BE.entity.BoardConfig;
import GBFH.GBFH_BE.exception.FileSizeIsNotAllowedException;
import GBFH.GBFH_BE.exception.NotAllowedExtensionException;
import GBFH.GBFH_BE.repository.BoardConfigRepository;
import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardConfigService {
    private final BoardConfigRepository boardConfigRepository;

    public List<BoardConfigDTO> getAllBoardConfig() {
        List<BoardConfig> boardConfigList = boardConfigRepository.findAll();

        return boardConfigList.stream()
                .map(BoardConfigDTO::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public boolean isAllowedContentType(String contentType, String boardId) {
        List<String> extensionList = changeExtensionToList(boardConfigRepository.getReferenceById(boardId).getFileAllowExtension());
        // 일치하지 않는 파일 확장자라면?
        if (extensionList.stream().anyMatch(item -> item.equalsIgnoreCase(contentType))) {
            throw new NotAllowedExtensionException(contentType + "은 올바르지 않은 형식의 파일입니다.");
        } else {
            return true;
        }
    }

    public boolean isFileSizeAllowed(String fileSize, String boardId) {
        if (Integer.parseInt(boardConfigRepository.getReferenceById(boardId).getFileLimitSize()) < Integer.parseInt(fileSize)) {
            throw new FileSizeIsNotAllowedException("파일 크기가 너무 큽니다.");
        } else {
            return true;
        }
    }

    public List<String> changeExtensionToList(String ext) {
        return Arrays.asList(ext.split("\\s*,\\s*"));
    }
}
