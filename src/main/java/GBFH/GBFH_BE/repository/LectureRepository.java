package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Lecture;
import GBFH.GBFH_BE.entity.LectureGetDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> {
    List<Lecture> findAllByOpenAndRegIngOrderByCreateDtDesc(char open, char regIng);

    @Override
    Optional<Lecture> findById(String s);

    List<Lecture> findAllByOpenAndRegIngAndStartDtGreaterThanEqualAndEndDtLessThanEqual(
            Character open, Character regIng, LocalDate startDt, LocalDate endDt);
}
