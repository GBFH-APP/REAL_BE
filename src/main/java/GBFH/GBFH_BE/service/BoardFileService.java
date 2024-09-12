package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileResponseDTO;
import GBFH.GBFH_BE.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardFileService {
        private final BoardFileRepository boardFileRepository;

        public boolean isExistFile(Long IDX) {
            return boardFileRepository.existsAllByIDX(IDX);
        }

        public FileResponseDTO getAllFileDTO(Long IDX) {
            return FileResponseDTO.toDTOList(boardFileRepository.findAllByIDX(IDX));
        }
}
