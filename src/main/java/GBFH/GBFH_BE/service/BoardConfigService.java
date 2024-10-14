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
import java.util.Objects;
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

    public boolean isAllowedContentType(String extension, String boardId, String contentType) {
        List<String> extensionList = changeExtensionToList(boardConfigRepository.getReferenceById(boardId).getFileAllowExtension());

        // MIME 타입 매핑
        List<String> mimeTypeList = extensionList.stream()
                .map(BoardConfigDTO::getMimeType)  // 확장자를 MIME 타입으로 변환
                .filter(Objects::nonNull)  // 유효한 MIME 타입만 남기기
                .toList();


        // 일치하지 않는 파일 확장자라면?
        boolean isNotAllowedExtension = extensionList.stream().anyMatch(item -> item.equalsIgnoreCase(extension));
        //MIMETYPE과 일치하지 않는 파일이라면?
        boolean isNotAllowedMimeType = mimeTypeList.stream().anyMatch(mime -> mime.equalsIgnoreCase(contentType));


        if (!isNotAllowedExtension || !isNotAllowedMimeType) {
            // 확장자 또는 MIME 타입이 허용되지 않는 경우 예외 발생
            throw new NotAllowedExtensionException(contentType + "은 올바르지 않은 형식의 파일입니다.");
        } else {
            return true;
        }
    }

    public boolean isFileSizeAllowed(String fileSize, String boardId) {

        int maxFileSizeInMb = Integer.parseInt(boardConfigRepository.getReferenceById(boardId).getFileLimitSize());  // 파일 제한 크기 (MB 단위)
        int fileSizeInMb = Integer.parseInt(fileSize);  // 업로드된 파일 크기 (MB 단위)
        System.out.println(fileSizeInMb + "파일 용량");
        if (fileSizeInMb > maxFileSizeInMb) {
            throw new FileSizeIsNotAllowedException("파일 크기가 너무 큽니다.");
        } else {
            return true;
        }
    }

    public List<String> changeExtensionToList(String ext) {
        return Arrays.asList(ext.split("\\s*,\\s*"));
    }
}
