package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.lost.CreateLostDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.repository.BoardRepository;
import GBFH.GBFH_BE.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;

    @PostMapping()
    public ResponseEntity<ResponseDTO> findAllEvidenceByMonth(@RequestBody CreateLostDTO createLostDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CreateLostDTO.Res response = lostService.createLost(createLostDTO, username);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_LOST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_LOST, response));
    }
}
