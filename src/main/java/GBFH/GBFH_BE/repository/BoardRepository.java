package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByIdx(Long idx);
    List<Board> findAllByBoardIdAndNotiAndNotiEndAfterAndNotiStartBefore(BoardId boardId, Integer noti, String now, String today);

    /**
     * lost
     */
    List<Board> findAlByBoardIdOrderByIdxDesc(BoardId boardId);


    List<Board> findAllByBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(BoardId boardId, Integer noti, String now, String today);

    // idx 필드의 최대값을 조회
    @Query("SELECT COALESCE(MAX(e.idx), 0) FROM Board e")
    Long findMaxIdx();

    // grp 필드의 최대값을 조회
    @Query("SELECT COALESCE(MAX(e.grp), 0) FROM Board e")
    Long findMaxGrp();

    List<Board> findAllByBoardIdAndNotiOrderByCreateDTDesc(BoardId boardId, Integer noti);

    List<Board> findAllByTitleContainingAndBoardIdAndNotiOrderByCreateDTDesc(String title, BoardId boardId, Integer noti);

    List<Board> findAllByTitleContainingAndBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(String title, BoardId boardId, Integer noti, String notiEnd, String notiStart);
}

