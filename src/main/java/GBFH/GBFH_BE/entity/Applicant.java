package GBFH.GBFH_BE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "t_applicant")
public class Applicant {
    @Column(name = "USER_NO", columnDefinition = "BIGINT")
    @Id
    private Long userNo;

    @Column(name = "LOGIN_ID")
    private String loginId;

    @Column(name = "LOGIN_PWD")
    private String loginPwd;
}
