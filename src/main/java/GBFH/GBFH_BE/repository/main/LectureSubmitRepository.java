package GBFH.GBFH_BE.repository.main;

import GBFH.GBFH_BE.entity.main.LectureSubmit;
import GBFH.GBFH_BE.entity.main.LectureSubmitPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LectureSubmitRepository extends JpaRepository<LectureSubmit, LectureSubmitPk> {
    List<LectureSubmit> findAllByRegiNoAndCreateDTBetweenOrderByCreateDTDesc(String regiNo, LocalDateTime start, LocalDateTime end);
}
