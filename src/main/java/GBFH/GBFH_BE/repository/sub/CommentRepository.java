package GBFH.GBFH_BE.repository.sub;

import GBFH.GBFH_BE.entity.sub.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // grp 필드의 최대값을 조회. 값이 없을 경우 1로 반환
    @Query("SELECT COALESCE(MAX(e.grp), 0) FROM Comment e")
    Long findMaxGrp();

    // 댓글 조회
    List<Comment> findByLostBoardIdxAndDelYNAndLvl(Long upIdx, String DelYN, Long lvl);

    // 댓글 조회 lvl 고려 x
    List<Comment> findByLostBoardIdxAndDelYN(Long upIdx, String DelYN);

    // 대댓글 조회
    List<Comment> findByLostBoardIdxAndDelYNAndLvlAndGrp(Long upIdx, String DelYN, Long lvl, Long grp);

    Optional<Comment> findByIdxAndDelYN(Long Idx, String delYN);
}
