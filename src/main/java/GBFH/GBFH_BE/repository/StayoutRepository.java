package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Stayout;
import GBFH.GBFH_BE.entity.StayoutPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayoutRepository extends JpaRepository<Stayout, StayoutPk> {
    Boolean existsByRegiNo(String regi);
    List<Stayout> findAllByRegiNoOrderBySeqDesc(String regi);

}
