package GBFH.GBFH_BE.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class StayoutPk implements Serializable {
    private String regiNo; //글 아이디
    private Integer seq; //순서

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StayoutPk that = (StayoutPk) o;
        return Objects.equals(regiNo, that.regiNo) && Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regiNo, seq);
    }
}
