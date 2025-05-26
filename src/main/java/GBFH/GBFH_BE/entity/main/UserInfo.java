package GBFH.GBFH_BE.entity.main;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Entity
@Getter
@Table(name = "v_app_dormitorian")
@Immutable
public class UserInfo {
    @Id
    @Column(name = "USER_NO", length = 20)
    private String userNo;

    @Column(name = "NAME_KOR", length = 30)
    private String nameKor;

    @Column(name = "HP_NO", length = 30)
    private String hpNo;

    @Column(name = "PK_ROOMNO", length = 20)
    private String roomNo; //호수

    @Column(name = "HOUSE", length = 5)
    @Enumerated(EnumType.STRING)
    private House house;

}
