package GBFH.GBFH_BE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "t_board_config")
public class BoardConfig {
    // 게시판 pk
    @Column(name = "BOARD_ID")
    @Id
    private String boardId;

    // 게시판 명칭
    @Column(name = "BOARD_NAME")
    private String boardName;
}
