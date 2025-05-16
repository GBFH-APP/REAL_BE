package GBFH.GBFH_BE.dto.board;

import GBFH.GBFH_BE.entity.BoardSummary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@Builder
public class NoticeListResponseDTO {
    private final String id; //idx
    private final String title;
    private final String writer;
    private final Long read;
    private final LocalDate createAt; //CREATE_DT

    // 파일 추가 필요
    public static NoticeListResponseDTO toSummaryDTO(BoardSummary board) {
        return NoticeListResponseDTO.builder()
                .id(board.getIdx().toString())
                .title(board.getTitle())
                .writer(board.getWriter())
                .read(board.getRead())
                .createAt(board.getCreateDT().toLocalDate()).build();
    }
}
