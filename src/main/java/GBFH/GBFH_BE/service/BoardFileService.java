package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileResponseDTO;
import GBFH.GBFH_BE.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardFileService {
        private final BoardFileRepository boardFileRepository;

        public boolean isExistFile(Long idx) {
            return boardFileRepository.existsAllByIdx(idx);
        }

        public FileResponseDTO getAllFileDTO(Long idx) {
            return FileResponseDTO.toDTOList(boardFileRepository.findAllByIdx(idx));
        }
}
