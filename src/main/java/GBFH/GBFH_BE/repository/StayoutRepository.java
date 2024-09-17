package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Stayout;
import GBFH.GBFH_BE.entity.StayoutPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StayoutRepository extends JpaRepository<Stayout, StayoutPk> {

}
