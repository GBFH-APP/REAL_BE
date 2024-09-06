package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.applicant.CustomUserDetails;
import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ApplicantRepository applicantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        if (applicant != null) {

            return new CustomUserDetails(applicant);
        }


        return null;
    }
}
