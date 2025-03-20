package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardId;
import GBFH.GBFH_BE.entity.BoardSummary;
import GBFH.GBFH_BE.entity.SimpleNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 이전글
    SimpleNotice findTopByCreateDTLessThanAndBoardIdOrderByCreateDTDesc(LocalDateTime createDT, BoardId boardId);
    SimpleNotice findFirstByBoardIdAndIdxAfterOrderByIdxAsc(BoardId boardId, Long idx);
    SimpleNotice findFirstByBoardIdAndIdxBeforeOrderByIdxDesc(BoardId boardId, Long idx);
    // 다음글
    SimpleNotice findTopByCreateDTGreaterThanAndBoardIdOrderByCreateDTDesc(LocalDateTime createDT, BoardId boardId);
    Optional<Board> findByIdx(Long idx);
    // trashYN 고려함
    Optional<Board> findByIdxAndTrashYN(Long idx, Character trashYN);
    List<Board> findAllByBoardIdAndNotiAndNotiEndAfterAndNotiStartBefore(BoardId boardId, Integer noti, String now, String today);

    /**
     * lost
     */
    List<Board> findAllByBoardIdAndTrashYNOrderByIdxDesc(BoardId boardId, Character trashYN);
    List<Board> findAllByBoardIdAndStatusOrderByIdxDesc(BoardId boardId, String status);
    List<Board> findAllByBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(BoardId boardId, Integer noti, String now, String today);

    @Query("SELECT e FROM Board e WHERE e.boardId = :boardId AND (e.title LIKE %:keyword% OR e.contents LIKE %:keyword%) ORDER BY e.createDT DESC")
    List<Board> findByBoardIdAndTitleOrContentsContainingAndOrderByCreateDTDesc(@Param("boardId") BoardId boardId, @Param("keyword") String keyword);

    // idx 필드의 최대값을 조회
    @Query("SELECT COALESCE(MAX(e.idx), 0) FROM Board e")
    Long findMaxIdx();

    // grp 필드의 최대값을 조회
    @Query("SELECT COALESCE(MAX(e.grp), 0) FROM Board e")
    Long findMaxGrp();

    List<BoardSummary> findByBoardIdAndNotiOrderByCreateDTDesc(BoardId boardId, Integer noti);

    List<BoardSummary> findByTitleContainingAndBoardIdAndNotiOrderByCreateDTDesc(String title, BoardId boardId, Integer noti);

    List<Board> findAllByTitleContainingAndBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(String title, BoardId boardId, Integer noti, String notiEnd, String notiStart);
}

