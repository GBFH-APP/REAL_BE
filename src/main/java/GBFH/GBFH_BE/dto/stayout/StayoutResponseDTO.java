package GBFH.GBFH_BE.dto.stayout;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StayoutResponseDTO {
    private String regiNo;
    private Integer seq;

    private LocalDateTime startDT;
    private LocalDateTime endDT;
    private LocalDateTime returnDT; //중도 복귀일
    //생성시에는 null 넣으면 됨

    private String reason;

    private Integer status;
    // N 이면 신청 //
    // Y 면 승인
    // 승인이면서 복귀일 존재 = > 복귀 완료
    // Y인데 enddt 보다 지났는데 복귀일이 null이라면 미복귀

    private LocalDateTime createDT; //작성일
}
