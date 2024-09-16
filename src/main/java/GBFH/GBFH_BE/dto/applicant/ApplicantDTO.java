package GBFH.GBFH_BE.dto.applicant;

import GBFH.GBFH_BE.entity.Applicant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ApplicantDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Res {
        private String userNo;
        private String loginId;

        public static Res mapToRes(Applicant applicant) {
            return Res.builder()
                    .userNo(applicant.getUserNo())
                    .loginId(applicant.getLoginId())
                    .build();
        }
    }
}
