package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.code.ResponseCode;
import GBFH.GBFH_BE.dto.menu.GetMenuDTO;
import GBFH.GBFH_BE.dto.response.ResponseDTO;
import GBFH.GBFH_BE.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    // 일 별로 메뉴 조회
    @GetMapping("/week")
    public ResponseEntity<ResponseDTO<?>> getWeekMenus(@RequestParam("date") String date) {
        List<GetMenuDTO> res = menuService.getWeekMenus(date);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_MENU.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_MENU, res));
    }

    // 하루 메뉴 조회
    @GetMapping("/day")
    public ResponseEntity<ResponseDTO<?>> getDayMenu(@RequestParam("date") String date) {
        GetMenuDTO res = menuService.getDayMenu(date);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_MENU.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_MENU, res));
    }
}
