package GBFH.GBFH_BE.repository.main;

import GBFH.GBFH_BE.entity.main.Board;
import GBFH.GBFH_BE.entity.main.BoardId;
import GBFH.GBFH_BE.entity.main.BoardSummary;
import GBFH.GBFH_BE.entity.main.SimpleNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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



    // grp 필드의 최대값을 조회
    @Query("SELECT COALESCE(MAX(e.grp), 0) FROM Board e")
    Long findMaxGrp();

    List<BoardSummary> findByBoardIdAndNotiOrderByCreateDTDesc(BoardId boardId, Integer noti);

    List<BoardSummary> findByTitleContainingAndBoardIdAndNotiOrderByCreateDTDesc(String title, BoardId boardId, Integer noti);

    List<BoardSummary> findAllByTitleContainingAndBoardIdAndNotiAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(String title, BoardId boardId, Integer noti, String notiEnd, String notiStart);

    List<Board> findAllByBoardIdAndTrashYNAndStatusOrderByIdxDesc(BoardId boardId, Character n, String status);
    List<BoardSummary> findAllByBoardIdAndNotiAndTitleNotContainingAndNotiEndAfterAndNotiStartBeforeOrderByCreateDTDesc(
            BoardId boardId, int noti, String titleKeyword, String notiEnd, String notiStart);

    List<BoardSummary> findByBoardIdAndNotiAndTitleNotContainingOrderByCreateDTDesc(BoardId boardId, int noti, String titleKeyword);

    SimpleNotice findFirstByBoardIdAndCreateDTBeforeAndTitleContainingOrderByCreateDTDesc(BoardId boardId, LocalDateTime createDT, String keyword);
    SimpleNotice findFirstByBoardIdAndCreateDTAfterAndTitleContainingOrderByCreateDTAsc(BoardId boardId, LocalDateTime createDT, String keyword);
    SimpleNotice findFirstByBoardIdAndCreateDTBeforeAndTitleNotContainingOrderByCreateDTDesc(BoardId boardId, LocalDateTime createDT, String keyword);
    SimpleNotice findFirstByBoardIdAndCreateDTAfterAndTitleNotContainingOrderByCreateDTAsc(BoardId boardId, LocalDateTime createDT, String keyword);


    // 공통 조건 처리용 메서드
    @Query("""
        SELECT n FROM Board n
        WHERE n.boardId = :boardId
        AND n.title LIKE %:title%
        AND (
            n.noti = 0
            OR (n.noti = 1 AND n.notiEnd < :today)
        )
        ORDER BY n.createDT DESC
        """)
    List<BoardSummary> findByBoardIdAndTitleContainingAndNotiCondition(
            @Param("boardId") BoardId boardId,
            @Param("title") String title,
            @Param("today") String today
    );

    // 채용 제외용
    @Query("""
        SELECT n FROM Board n
        WHERE n.boardId = :boardId
        AND n.title NOT LIKE %:excludedTitle%
        AND (
            n.noti = 0
            OR (n.noti = 1 AND n.notiEnd < :today)
        )
        ORDER BY n.createDT DESC
        """)
    List<BoardSummary> findByBoardIdAndTitleNotContainingAndNotiCondition(
            @Param("boardId") BoardId boardId,
            @Param("excludedTitle") String excludedTitle,
            @Param("today") String today
    );


}

