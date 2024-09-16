package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.exception.WrongPaginationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaginateService {
    public <T> Page<T> paginateList(List<T> list, int page, int size) {
        // 요청한 페이지가 전체 데이터 수를 초과할 때 빈 페이지 반환
        int start = Math.min(page * size, list.size());
        if (start >= list.size()) {
            throw new WrongPaginationException("페이지가 너무 큽니다.");
        }
        int end = Math.min((page + 1) * size, list.size());
        List<T> paginatedList = list.subList(start, end);

        return new PageImpl<>(paginatedList, PageRequest.of(page, size), list.size());
    }

}
