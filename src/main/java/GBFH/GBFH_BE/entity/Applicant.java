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

}
