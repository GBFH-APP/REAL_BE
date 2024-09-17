package GBFH.GBFH_BE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "t_dorm_enter_submit")
@Getter
public class DormEnterSubmit {
    @Id
    @Column(name = "REGISTRATION_NO", length = 20)
    private String regiNo;

    @Column(name = "YEAR_TERM", length = 4)
    private String yearTerm;

    @Column(name = "TRACK_NO")
    private Integer trackNo; //등록차수

    @Column(name = "REG_STATUS")
    private Character regStatus; // (7:신청,5:포기,4:대기,3:선발,1:배정,0:퇴관)

    @Column(name = "NAME_KOR", length = 30)
    private String name; //한국 이름

    // 등록자
    @Column(name = "CREATE_ID", length = 60)
    private String createId;
}
