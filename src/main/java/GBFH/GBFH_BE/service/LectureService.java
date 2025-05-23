package GBFH.GBFH_BE.service;


import GBFH.GBFH_BE.dto.lecture.LectureResponseDTO;
import GBFH.GBFH_BE.dto.lecture.LectureSubmitResponseDto;
import GBFH.GBFH_BE.entity.*;
import GBFH.GBFH_BE.exception.DuplicateLectureSubmitException;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.DormEnterSubmitRepository;
import GBFH.GBFH_BE.repository.LectureRepository;
import GBFH.GBFH_BE.repository.LectureSubmitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LectureService {
    private final LectureRepository lectureRepository;
    private final PaginateService paginateService;
    private final ApplicantRepository applicantRepository;
    private final DormEnterSubmitRepository dormEnterSubmitRepository;
    private final LectureSubmitRepository lectureSubmitRepository;

    public Page<LectureResponseDTO> getAllLecture(char yorn, int page, int size) {
        List<Lecture> lectures = lectureRepository.findAllByOpenAndRegIngOrderByCreateDtDesc('Y', yorn);

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

    public LectureSubmitResponseDto createLectureSubmit(String username, String id, String clientIp) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 글이 존재하지 않습니다."));

        // 입사한 사람
        DormEnterSubmit dormEnterSubmit = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo());

        String regNo = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo()).getRegiNo();


        if (lectureSubmitRepository.existsById(new LectureSubmitPk(lecture.getIdx(), regNo))) {
            throw new DuplicateLectureSubmitException("중복된 신청임");
        }

        LectureSubmit lectureSubmit = LectureSubmit.builder()
                .idx(id)
                .regiNo(regNo)
                .status("등록")
                .createDT(LocalDateTime.now())
                .createIP(clientIp)
                .build();

        lectureSubmitRepository.save(lectureSubmit);

        return LectureSubmitResponseDto.builder()
                .id(lectureSubmit.getIdx())
                .nameKor(dormEnterSubmit.getName())
                .title(lecture.getTitle())
                .build();
    }

    public LectureSubmitResponseDto deleteLectureSubmit(String username, String id) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 글이 존재하지 않습니다."));

        DormEnterSubmit dormEnterSubmit = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo());

        lectureSubmitRepository.deleteById(new LectureSubmitPk(lecture.getIdx(), dormEnterSubmit.getRegiNo()));
        return LectureSubmitResponseDto.builder()
                .id(lecture.getIdx())
                .nameKor(dormEnterSubmit.getName())
                .title(lecture.getTitle())
                .build();
    }

    public List<Integer> getLectureMontlyList(Integer month) {
        YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);

        LocalDate startDate = yearMonth.atDay(1); // 그 달의 첫 날
        LocalDate endDate = yearMonth.atEndOfMonth(); // 그 달의 마지막 날

        List<Lecture> lectures = lectureRepository.findAllByOpenAndRegIngAndStartDtGreaterThanEqualAndEndDtLessThanEqual('Y','Y', startDate, endDate);
        return lectures.stream().map((lecture -> {
            return lecture.getStartDt().getDayOfMonth();
        })).collect(Collectors.toList());
    }

    // 달마다? 하나?


    ///////////// 특강 신청
}
