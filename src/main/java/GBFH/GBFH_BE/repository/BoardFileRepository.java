package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.BoardFile;
import GBFH.GBFH_BE.entity.BoardFilePk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, BoardFilePk> {
    Boolean existsAllByIdx(Long idx);
    List<BoardFile> findAllByIdx(Long Idx);
}
