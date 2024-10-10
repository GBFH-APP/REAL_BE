package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.boardconfig.BoardConfigDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.service.BoardConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/board-config")
@RequiredArgsConstructor
public class BoardConfigController {
    private final BoardConfigService boardConfigService;

    @GetMapping
    public ResponseEntity<ResponseDTO<?>> getAllBoardConfig() {
        List<BoardConfigDTO> response = boardConfigService.getAllBoardConfig();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_BOARD_CONFIG.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_BOARD_CONFIG, response));
    }
}
