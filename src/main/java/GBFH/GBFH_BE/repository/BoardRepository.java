package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdx(Long idx);
    List<Board> findAllByBoardIdAndNotiAndNotiEndAfterAndNotiStartBefore(String boardId, Integer noti, String now, String today);

}
