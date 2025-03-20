package GBFH.GBFH_BE.dto.board;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimplePostDTO {
    private String idx;
    private String title;
}
