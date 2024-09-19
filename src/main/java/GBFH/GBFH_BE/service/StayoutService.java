package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.entity.DormEnterSubmit;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.DormEnterSubmitRepository;
import GBFH.GBFH_BE.repository.StayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StayoutService {
    private final StayoutRepository stayoutRepository;
    private final ApplicantRepository applicantRepository;
    private final DormEnterSubmitRepository dormEnterSubmitRepository;


    public void getAllStayout(String username) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));


        DormEnterSubmit dormEnterSubmit = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo());
        System.out.println(dormEnterSubmit);
    }
}
