package GBFH.GBFH_BE.service;


import GBFH.GBFH_BE.dto.lecture.LectureResponseDTO;
import GBFH.GBFH_BE.entity.Lecture;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final PaginateService paginateService;

    public Page<LectureResponseDTO> getAllLecture(int page, int size) {
        List<Lecture> lectures = lectureRepository.findAllByOpenOrderByCreateDtDesc('Y');

        if (lectures.isEmpty()) {
            throw new EmptyPostException("글이 비었습니다.");
        }
        else {
            List<LectureResponseDTO> lectureResponseDTOS = lectures.stream().map((LectureResponseDTO::toDto)).toList();
            return paginateService.paginateList(lectureResponseDTOS, page, size);
        }
    }

    public LectureResponseDTO getLectureDetail(String id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 글이 존재하지 않습니다."));

        return LectureResponseDTO.toDto(lecture);
    }
}
