package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.entity.main.BoardId;
import GBFH.GBFH_BE.exception.category_BoardIdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardIdService {

    // Category 찾는 메소드
    public BoardId getCategory(String category) {
        return Arrays.stream(BoardId.values())
                .filter(e -> e.name().equalsIgnoreCase(category))  // 대소문자 구분 없이 비교
                .findFirst()
                .orElseThrow(() -> new category_BoardIdNotFoundException("Category not found: " + category));  // Custom Exception 발생
    }
}