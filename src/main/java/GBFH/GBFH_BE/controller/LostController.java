package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.lost.CreateLostDTO;
import GBFH.GBFH_BE.dto.lost.GetLostDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.service.LostService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> createLost(
            @Valid @RequestPart("createLostDTO") CreateLostDTO createLostDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpServletRequest request) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String clientIp = getClientIP(request);

        CreateLostDTO.Res res = lostService.createLost(createLostDTO, username, clientIp, files);
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

    @GetMapping
    public ResponseEntity<ResponseDTO> getLostsByStatus(@PathParam("status") String status) {
        List<GetLostDTO.LIST> res = lostService.getLostsByStatus(status);

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
