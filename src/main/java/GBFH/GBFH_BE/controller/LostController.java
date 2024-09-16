package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lost")
@RequiredArgsConstructor
public class LostController {
    private final BoardRepository boardRepository;


}
