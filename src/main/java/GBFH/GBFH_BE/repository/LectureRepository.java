package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> {
    List<Lecture> findAllByOpenOrderByCreateDtDesc(char open);
}
