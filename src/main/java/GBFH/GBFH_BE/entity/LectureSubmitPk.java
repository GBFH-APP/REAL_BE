package GBFH.GBFH_BE.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class LectureSubmitPk implements Serializable {
    private String idx; // 글 아이디
    private String regiNo; // 학생 regiNo

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureSubmitPk that = (LectureSubmitPk) o;
        return Objects.equals(idx, that.idx) && Objects.equals(regiNo, that.regiNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idx, regiNo);
    }
}
