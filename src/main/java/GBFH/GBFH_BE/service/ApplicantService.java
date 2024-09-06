package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.applicant.ApplicantDTO;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public List<ApplicantDTO.Res> getApplicants() {
        return applicantRepository.findAll().stream().map(ApplicantDTO.Res::mapToRes).collect(Collectors.toList());
    }
}
