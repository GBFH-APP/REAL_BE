package GBFH.GBFH_BE.repository.main;

import GBFH.GBFH_BE.entity.main.DormEnterSubmit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DormEnterSubmitRepository extends JpaRepository<DormEnterSubmit, String> {
    Optional<DormEnterSubmit> findTopByCreateIdOrderByTrackNoDesc(String createId);
}
