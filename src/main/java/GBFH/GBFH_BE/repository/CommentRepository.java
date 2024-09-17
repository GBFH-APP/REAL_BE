package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // grp 필드의 최대값을 조회. 값이 없을 경우 1로 반환
    @Query("SELECT COALESCE(MAX(e.grp), 0) FROM Comment e")
    Long findMaxGrp();

    List<Comment> findByUpIdxAndDelYN(Long upIdx, String DelYN);

}
