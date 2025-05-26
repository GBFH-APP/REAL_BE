package GBFH.GBFH_BE.repository.sub;

import GBFH.GBFH_BE.entity.main.BoardSummary;
import GBFH.GBFH_BE.entity.sub.LostBoard;
import GBFH.GBFH_BE.entity.sub.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LostBoardRepository extends JpaRepository<LostBoard, Long> {
    /**
     * lost
     */
    List<LostBoard> findAllByTrashYNOrderByIdxDesc(Character trashYN);
    List<LostBoard> findAllByStatusOrderByIdxDesc(Status status);

    @Query("SELECT e FROM LostBoard e WHERE (e.title LIKE %:keyword% OR e.contents LIKE %:keyword%) ORDER BY e.createDT DESC")
    List<LostBoard> findByTitleOrContentsContainingAndOrderByCreateDTDesc(@Param("keyword") String keyword);

    // idx 필드의 최대값을 조회
    @Query("SELECT COALESCE(MAX(e.idx), 0) FROM LostBoard e")
    Long findMaxIdx();

    Optional<LostBoard> findByIdxAndTrashYN(Long idx, Character trashYN);

    List<LostBoard> findAllByTrashYNAndStatusOrderByIdxDesc(Character trashYN, Status status);
}
