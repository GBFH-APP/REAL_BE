package GBFH.GBFH_BE.repository.sub;

import GBFH.GBFH_BE.entity.main.BoardFile;
import GBFH.GBFH_BE.entity.sub.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    Boolean existsAllByIdx(Long idx);
    List<ImageFile> findAllByIdx(Long Idx);
    Optional<ImageFile> findByFileId(String fileId);
    @Query("SELECT COALESCE(MAX(e.seq), 0) FROM ImageFile e WHERE e.idx = :idx")
    Long findMaxGrpByIdx(@Param("idx") Long idx);

}
