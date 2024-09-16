package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.lost.CreateLostDTO;
import GBFH.GBFH_BE.dto.lost.GetLostDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;

    @PostMapping()
    public ResponseEntity<ResponseDTO> createLost(@RequestBody CreateLostDTO createLostDTO, HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String clientIp = getClientIP(request);

        CreateLostDTO.Res res = lostService.createLost(createLostDTO, username, clientIp);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_LOST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_LOST, res));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllLosts() {
        List<GetLostDTO.LIST> res = lostService.getAllLosts();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST, res));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseDTO> getDetailLosts(@PathVariable("id") Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        GetLostDTO.DETAIL res = lostService.getDetailLost(id, username);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_LOST_LIST, res));
    }

    private static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
