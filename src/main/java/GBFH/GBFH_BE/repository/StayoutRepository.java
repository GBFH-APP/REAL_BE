package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Stayout;
import GBFH.GBFH_BE.entity.StayoutPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StayoutRepository extends JpaRepository<Stayout, StayoutPk> {
    Boolean existsByRegiNo(String regi);
    List<Stayout> findAllByRegiNoOrderBySeqDesc(String regi);

    @Query("SELECT COALESCE(MAX(s.seq), 0) FROM Stayout s WHERE s.regiNo = :regi")
    Integer findMaxSeq(@Param("regi") String regi);

    @Override
    Optional<Stayout> findById(StayoutPk stayoutPk);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Stayout e WHERE FUNCTION('DATE', e.startDT) <= :targetDate AND FUNCTION('DATE', e.endDT) >= :targetDate")
    boolean existsByDateInRange(@Param("targetDate") LocalDate targetDate);
}
