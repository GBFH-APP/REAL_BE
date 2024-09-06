package GBFH.GBFH_BE.dto.board;

import GBFH.GBFH_BE.entity.Board;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponseDTO {
    private Long id; //idx
    private String title;
    private String content; //contents
    private String writer;
    private Long read;
    private LocalDate createAt; //CREATE_DT
    //파일 추가 필요

    public static NoticeResponseDTO toDTO (Board board) {
        return NoticeResponseDTO.builder()
                .id(board.getIDX())
                .title(board.getTITLE())
                .content(board.getCONTENTS())
                .createAt(board.getCREATE_DT().toLocalDate())
                .read(board.getREAD())
                .writer(board.getMASK_WRITER())
                .build();
    }
}
