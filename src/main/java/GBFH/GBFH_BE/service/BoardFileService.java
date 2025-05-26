package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.entity.main.BoardFile;
import GBFH.GBFH_BE.mapper.FileMapper;
import GBFH.GBFH_BE.repository.main.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardFileService {
        private final BoardFileRepository boardFileRepository;
        private final FileMapper mapper;

        public boolean isExistFile(Long idx) {
            return boardFileRepository.existsAllByIdx(idx);
        }

        public List<FileDTO> getAllFileDTO(Long idx) {
            List<BoardFile> files = boardFileRepository.findAllByIdx(idx);
            return files.stream().map(mapper::toDto).collect(Collectors.toList());
        }
}
