package GBFH.GBFH_BE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "t_board")
public class Board {
    @Column(name = "IDX", columnDefinition = "BIGINT")
    @Id
    private Long IDX;

    public Long getIdx() {
        return IDX;
    }
}
