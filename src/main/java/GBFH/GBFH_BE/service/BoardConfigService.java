package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.boardconfig.BoardConfigDTO;
import GBFH.GBFH_BE.entity.main.BoardConfig;
import GBFH.GBFH_BE.repository.main.BoardConfigRepository;
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
}
