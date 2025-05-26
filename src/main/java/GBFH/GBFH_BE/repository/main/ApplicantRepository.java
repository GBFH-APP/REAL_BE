package GBFH.GBFH_BE.repository.main;

import GBFH.GBFH_BE.entity.main.Applicant;
import GBFH.GBFH_BE.entity.main.ApplicantSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Boolean existsByLoginId(String loginId);
    Optional<ApplicantSummary> findSummaryByLoginId(String loginId);
    Optional<Applicant> findByLoginId(String loginId);
}
