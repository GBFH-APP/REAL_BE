package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.dto.applicant.CustomUserDetails;
import GBFH.GBFH_BE.service.StayoutService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stayout")
public class StayoutController {
    private final StayoutService stayoutService;

    @GetMapping("/all")
    //내 거 전체 가져옴
    public void getAllStayout(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        System.out.println(customUserDetails.getUsername());
    }
}
