package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "t_board_file")
@Getter
@IdClass(BoardFilePk.class) // 복합키 클래스
public class BoardFile {
    @Column(name = "IDX", columnDefinition = "BIGINT")
    @Id
    private Long IDX;

    @Column(name = "SEQ", columnDefinition = "BIGINT")
    @Id
    private Long SEQ;

    @Column(name="FILE_ID")
    private String FILE_ID; //이건 max 에서 +1 하라고 하심

}
