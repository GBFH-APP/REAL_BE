package GBFH.GBFH_BE.repository.main;

import GBFH.GBFH_BE.entity.main.Lecture;
import GBFH.GBFH_BE.entity.main.LectureGetDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> {
    List<Lecture> findAllByOpenAndRegIngAndYearOrderByCreateDtDesc(Character open, Character regIng, String year);

    @Override
    Optional<Lecture> findById(String s);

    List<Lecture> findAllByOpenAndRegIngAndStartDtGreaterThanEqualAndEndDtLessThanEqual(
            Character open, Character regIng, LocalDate startDt, LocalDate endDt);

    @Query(value = """
    SELECT *
    FROM t_lecture
    WHERE STR_TO_DATE(CONCAT(REPLACE(reg_end_ymd, '-', ''), LPAD(reg_end_hh, 2, '0'), LPAD(reg_end_mm, 2, '0')), '%Y%m%d%H%i')
          < DATE_FORMAT(NOW(), '%Y%m%d%H%i')
    """, nativeQuery = true)
    List<Lecture> findLecturesWithRecruitEnded();
}
