package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.BoardFile;
import GBFH.GBFH_BE.entity.BoardFilePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, BoardFilePk> {
    Boolean existsAllByIdx(Long idx);
    List<BoardFile> findAllByIdx(Long Idx);

    // idx 필드의 최대값을 조회
    @Query("SELECT COALESCE(MAX(e.idx), 0) FROM BoardFile e")
    Long findMaxIdx();
}
