package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.stayout.StayoutResponseDTO;
import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.entity.DormEnterSubmit;
import GBFH.GBFH_BE.entity.Stayout;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.DormEnterSubmitRepository;
import GBFH.GBFH_BE.repository.StayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StayoutService {
    private final StayoutRepository stayoutRepository;
    private final ApplicantRepository applicantRepository;
    private final DormEnterSubmitRepository dormEnterSubmitRepository;


    public List<StayoutResponseDTO> getAllStayout(String username) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        DormEnterSubmit dormEnterSubmit = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo());

        if (stayoutRepository.existsByRegiNo(dormEnterSubmit.getRegiNo())) {
            List<Stayout> stayouts = stayoutRepository.findAllByRegiNoOrderBySeqDesc(dormEnterSubmit.getRegiNo());
            return stayouts.stream().map((StayoutResponseDTO::toDTO)).collect(Collectors.toList());
        }
        else {
            throw new EmptyPostException("외박 신청 글이 존재하지 않습니다.");
        }
    }

    public StayoutResponseDTO createStayOut(String username) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        DormEnterSubmit dormEnterSubmit = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo());

    }
}
