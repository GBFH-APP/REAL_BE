package GBFH.GBFH_BE.dto.applicant;

import GBFH.GBFH_BE.entity.Applicant;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateApplicantDTO {
    @Getter
    public static class Req {
        // 연락처
        private String hpNo;
        // 혈액형
        private String bloodType;
        // 종교
        private String religion;
        // 취미
        private String interest;
        // 병역사항
        private String militaryYN;
        // 출신 고등학교명
        private String highSchool;
        // 졸업 년도
        private String highSchoolYear;
        // 은행 명
        private String refundBank;
        // 계좌번호
        private String refundAccount;
        // 예금주
        private String refundName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        // 연락처
        private String hpNo;
        // 혈액형
        private String bloodType;
        // 종교
        private String religion;
        // 취미
        private String interest;
        // 병역사항
        private String militaryYN;
        // 출신 고등학교명
        private String highSchool;
        // 졸업 년도
        private String highSchoolYear;
        // 은행 명
        private String refundBank;
        // 계좌번호
        private String refundAccount;
        // 예금주
        private String refundName;

        public static Res toDTO(Applicant applicant) {
            return Res.builder()
                    .hpNo(applicant.getHpNo())
                    .bloodType(applicant.getBloodType())
                    .religion(applicant.getReligion())
                    .interest(applicant.getInterest())
                    .militaryYN(applicant.getMilitaryYN())
                    .highSchool(applicant.getHighSchool())
                    .highSchoolYear(applicant.getHighSchoolYear())
                    .refundBank(applicant.getRefundBank())
                    .refundAccount(applicant.getRefundAccount())
                    .refundName(applicant.getRefundName())
                    .build();
        }
    }
}
