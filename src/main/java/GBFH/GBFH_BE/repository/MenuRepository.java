package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, String> {
    Optional<Menu> findByYmd(String ymd);

    @Query("SELECT m FROM Menu m WHERE m.ymd BETWEEN :startDate AND :endDate")
    List<Menu> findAllByWeek(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
