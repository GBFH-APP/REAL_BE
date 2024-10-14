package GBFH.GBFH_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "t_board_config")
public class BoardConfig {
    // 게시판 pk
    @Column(name = "BOARD_ID")
    @Enumerated(EnumType.STRING)
    @Id
    private BoardId boardId;

    // 게시판 명칭
    @Column(name = "BOARD_NAME")
    private String boardName;

    @Column(name = "FILE_ALLOW_EXT", length = 300)
    private String fileAllowExtension;

    @Column(name = "FILE_LIMIT_SIZE", length = 20)
    private String fileLimitSize;
}
