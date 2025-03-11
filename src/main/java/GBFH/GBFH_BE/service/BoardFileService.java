package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.entity.BoardFile;
import GBFH.GBFH_BE.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardFileService {
        private final BoardFileRepository boardFileRepository;

        public boolean isExistFile(Long idx) {
            return boardFileRepository.existsAllByIdx(idx);
        }

        public List<FileDTO> getAllFileDTO(Long idx) {
            List<BoardFile> files = boardFileRepository.findAllByIdx(idx);
            return files.stream().map(FileDTO::toDTO).collect(Collectors.toList());
        }
}
