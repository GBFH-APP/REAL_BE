package GBFH.GBFH_BE.dto.stayout;

import GBFH.GBFH_BE.entity.Stayout;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class StayoutResponseDTO {
    private String regiNo; // 학생 번호
    private Integer seq;

    private LocalDateTime startDT;
    private LocalDateTime endDT;
    private LocalDateTime returnDT; //중도 복귀일
    //생성시에는 null 넣으면 됨

    private String reason;

    private Integer status;
    // N 이면 신청 // 2
    // Y 면 승인 3
    // 승인이면서 복귀일 존재 = > 복귀 완료 4
    // Y인데 enddt 보다 지났는데 복귀일이 null이라면 미복귀 1

    private LocalDateTime createDT; //작성일

    public static StayoutResponseDTO toDTO(Stayout stayout) {
        return StayoutResponseDTO.builder()
                .regiNo(stayout.getRegiNo())
                .seq(stayout.getSeq())
                .startDT(stayout.getStartDT())
                .endDT(stayout.getEndDT())
                .returnDT(stayout.getReturnDT())
                .reason(stayout.getReason())
                .status(calculateStatus(stayout.getApproveType(), stayout.getReturnDT(), stayout.getEndDT()))
                .createDT(stayout.getCreateDT())
                .build();
    }

    public static Integer calculateStatus(Character approve, LocalDateTime returnDT, LocalDateTime endDT) {
        if ((approve == 'Y')) {
            if (returnDT != null) {
                return 4;
            }
            else if ((LocalDateTime.now().isAfter(endDT))) {
                return 1;
            }
            else {
                return 3;
            }
        }
        else if (approve == 'N') {
            return 2;
        }
        else {
            return 5; //반려
        }
    }
}
