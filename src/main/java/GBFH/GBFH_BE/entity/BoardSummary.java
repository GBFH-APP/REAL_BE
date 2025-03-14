package GBFH.GBFH_BE.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BoardSummary {
    Long getIdx();
    String getTitle();
    //String getContents();
    Long getRead();
    String getWriter();
    LocalDateTime getCreateDT();
}