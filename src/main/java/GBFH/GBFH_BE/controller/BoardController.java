package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;

    @GetMapping
    public List<Long> getBoard() {
        return boardRepository.findAll().stream().map(i -> i.getIdx()).collect(Collectors.toList());
    }
}
