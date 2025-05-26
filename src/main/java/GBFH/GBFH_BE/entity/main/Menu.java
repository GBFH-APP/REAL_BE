package GBFH.GBFH_BE.entity.main;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_cafeteria_menu")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    // 일자 - pk
    @Id
    @Column(name = "YMD")
    private String ymd;

    // 아침
    @Column(name = "BREAKFAST")
    private String breakfast;

    // 점심
    @Column(name = "LUNCH")
    private String lunch;

    // 저녁
    @Column(name = "DINNER")
    private String dinner;

}
