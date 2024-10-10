package GBFH.GBFH_BE.dto.stayout;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StayoutRequestDTO {
    @NotNull
    private LocalDate startDT;
    @NotNull
    private LocalDate endDT;
    @NotNull
    private String reason;
    //private LocalDateTime createDT; //작성일 - 시간은 안 보여주더라
}
