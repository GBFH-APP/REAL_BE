package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.applicant.ApplicantDTO;
import GBFH.GBFH_BE.dto.applicant.UpdateApplicantDTO;
import GBFH.GBFH_BE.dto.userInfo.UserInfoDto;
import GBFH.GBFH_BE.entity.main.Applicant;
import GBFH.GBFH_BE.entity.main.ApplicantSummary;
import GBFH.GBFH_BE.entity.main.UserInfo;
import GBFH.GBFH_BE.exception.NotInDormException;
import GBFH.GBFH_BE.repository.main.ApplicantRepository;
import GBFH.GBFH_BE.repository.main.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final UserInfoRepository userInfoRepository;

    public ApplicantDTO.DetailRes getApplicant(String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));
        return ApplicantDTO.DetailRes.mapToDetailRes(user);
    }

    public UserInfoDto getUserInfo(String username) {
        ApplicantSummary applicant = applicantRepository.findSummaryByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        UserInfo user = userInfoRepository.findByUserNo(applicant.getUserNo())
                .orElseThrow(() -> new NotInDormException("재사생만 이용할 수 있는 메뉴입니다."));

        return UserInfoDto.toDto(user);
    }

    @Transactional("mainTransactionManager")
    public UpdateApplicantDTO.Res updateApplicate(String username, UpdateApplicantDTO.Req updateApplicantDTO) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        applicant.updateInfo(updateApplicantDTO);
        applicantRepository.save(applicant);

        System.out.println(applicant.getRefundBank());

        return UpdateApplicantDTO.Res.toDTO(applicant);
    }
}
