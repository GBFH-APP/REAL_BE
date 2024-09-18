package GBFH.GBFH_BE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_applicant")
public class Applicant {
    @Column(name = "USER_NO", length = 20)
    @Id
    private String userNo;

    @Column(name = "LOGIN_ID")
    private String loginId;

    @Column(name = "LOGIN_PWD")
    private String loginPwd;

    @Column(name = "NAME_KOR")
    private String nameKor;

    // 생년월일 (없지만 드림)
    @Column(name = "BIRTHDAY")
    private String birthDate;

    // 성별 (없지만 드림)
    @Column(name = "GENDER")
    private String gender;

    // 연락처 - 휴대전화와 다른 점?
    @Column(name = "HP_NO")
    private String hpNo;

    // 혈액형
    @Column(name = "BLOOD_TYPE")
    private String bloodType;

    // 종교
    @Column(name = "RELIGION")
    private String religion;

    // 취미
    @Column(name = "INTEREST")
    private String interest;

    // 병역사항
    @Column(name = "MILITARY_YN")
    private String militaryYN;

    // 출신 고등학교명
    @Column(name = "HIGH_SCHOOL")
    private String highSchool;

    // 졸업 년도
    @Column(name = "HIGH_SCHOOL_YEAR")
    private String highSchoolYear;

    // 환불 계좌
    // 은행 명
    @Column(name = "REFUND_BANK")
    private String refundBank;

    // 계좌번호
    @Column(name = "REFUND_ACCOUNT")
    private String refundAccount;

    // 예금주
    @Column(name = "REFUND_NAME")
    private String refundName;

    // 입사생 서약서 동의 여부
    @Column(name = "AGREE_PROMISE")
    private String agreePromise;

    // 서약서 동의 일자
    @Column(name = "AGREE_PROMISE_DT")
    private String agreePromiseDT;

    // 생체정보 수집 동의 여부
    @Column(name = "AGREE_BIODATA")
    private String agreeBioData;

    // 생체정보 수집 동의서
    @Column(name = "AGREE_BIODATA_DT")
    private String agreeBioDataDT;
}
