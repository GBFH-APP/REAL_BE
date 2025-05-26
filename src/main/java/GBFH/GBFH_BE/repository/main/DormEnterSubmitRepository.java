package GBFH.GBFH_BE.repository.main;

import GBFH.GBFH_BE.entity.main.DormEnterSubmit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DormEnterSubmitRepository extends JpaRepository<DormEnterSubmit, String> {
    DormEnterSubmit findTopByCreateIdOrderByTrackNoDesc(String createId);
}
