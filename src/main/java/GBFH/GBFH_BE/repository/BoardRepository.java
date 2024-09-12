package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIDX(Long idx);

}
