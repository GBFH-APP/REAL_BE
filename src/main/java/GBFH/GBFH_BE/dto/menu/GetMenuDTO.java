package GBFH.GBFH_BE.dto.menu;

import GBFH.GBFH_BE.entity.main.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMenuDTO {
    private String ymd;
    private String breakfast;
    private String lunch;
    private String dinner;

    public static GetMenuDTO mapToDTO(Menu menu) {
        return GetMenuDTO.builder()
                .ymd(menu.getYmd())
                .breakfast(menu.getBreakfast())
                .lunch(menu.getLunch())
                .dinner(menu.getDinner())
                .build();
    }
}
