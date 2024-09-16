package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "t_board_file")
@Getter
@IdClass(BoardFilePk.class) // 복합키 클래스
public class BoardFile {
    @Column(name = "IDX")
    @Id
    private Long idx;

    @Column(name = "SEQ", columnDefinition = "BIGINT")
    @Id
    private Long seq;

    @Column(name="FILE_ID")
    private String fileId; //이건 max 에서 +1 하라고 하심

}
