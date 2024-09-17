package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.BoardFile;
import GBFH.GBFH_BE.entity.BoardFilePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardFileRepository extends JpaRepository<BoardFile, BoardFilePk> {
    Boolean existsAllByIdx(Long idx);
    List<BoardFile> findAllByIdx(Long Idx);
    Optional<BoardFile> findByFileId(String fileId);
    @Query("SELECT COALESCE(MAX(e.seq), 0) FROM BoardFile e WHERE e.idx = :idx")
    Long findMaxGrpByIdx(@Param("idx") Long idx);

}
