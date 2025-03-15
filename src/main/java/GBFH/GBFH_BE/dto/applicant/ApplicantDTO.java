package GBFH.GBFH_BE.dto.applicant;

import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.entity.ApplicantSummary;
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

        public static Res mapToResLog(ApplicantSummary applicant) {
            return Res.builder()
                    .userNo(applicant.getUserNo())
                    .loginId(applicant.getLoginId())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailRes {
        private String userNo;
        private String loginId;
        private String nameKor;
        private String birthDate;
        private String gender;
        private String hpNo;
        private String bloodType;
        private String religion;
        private String interest;
        private String militaryYN;
        private String highSchool;
        private String highSchoolYear;
        private String refundBank;
        private String refundAccount;
        private String refundName;
        private String agreePromise;
        private String agreePromiseDT;
        private String agreeBioData;
        private String agreeBioDataDT;

        public static DetailRes mapToDetailRes(Applicant applicant) {
            return DetailRes.builder()
                    .userNo(applicant.getUserNo())
                    .loginId(applicant.getLoginId())
                    .nameKor(applicant.getNameKor())
                    .birthDate(applicant.getBirthDate())
                    .gender(applicant.getGender())
                    .hpNo(applicant.getHpNo())
                    .bloodType(applicant.getBloodType())
                    .religion(applicant.getReligion())
                    .interest(applicant.getInterest())
                    .militaryYN(applicant.getMilitaryYN())
                    .highSchool(applicant.getHighSchool())
                    .highSchoolYear(applicant.getHighSchoolYear())
                    .refundAccount(applicant.getRefundAccount())
                    .refundName(applicant.getRefundName())
                    .agreePromise(applicant.getAgreePromise())
                    .agreePromiseDT(applicant.getAgreePromiseDT())
                    .agreeBioData(applicant.getAgreeBioData())
                    .agreeBioDataDT(applicant.getAgreeBioDataDT())
                    .build();
        }

        public static DetailRes mapToRes(ApplicantSummary applicant) {
            return DetailRes.builder()
                    .userNo(applicant.getUserNo())
                    .loginId(applicant.getLoginId())
                    .build();
        }
    }
}
