package GBFH.GBFH_BE.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class BoardFilePk implements Serializable {
    private Long IDX; //글 아이디
    private Long SEQ; //순서

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardFilePk that = (BoardFilePk) o;
        return Objects.equals(IDX, that.IDX) && Objects.equals(SEQ, that.SEQ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IDX, SEQ);
    }
}
