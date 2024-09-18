package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.applicant.ApplicantDTO;
import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public ApplicantDTO.DetailRes getApplicant(String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));
        return ApplicantDTO.DetailRes.mapToDetailRes(user);
    }
}
