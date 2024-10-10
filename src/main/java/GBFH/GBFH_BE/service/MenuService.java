package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.menu.GetMenuDTO;
import GBFH.GBFH_BE.entity.Menu;
import GBFH.GBFH_BE.exception.MenuNotFoundException;
import GBFH.GBFH_BE.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;


    public List<GetMenuDTO> getWeekMenus(String inputDate) {
        LocalDate date = LocalDate.parse(inputDate);
        LocalDate startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Menu> menus = menuRepository.findAllByWeek(startDate.toString(), endDate.toString());
        return menus.stream().map(GetMenuDTO::mapToDTO).toList();
    }

    public GetMenuDTO getDayMenu(String date) {
        Menu menu = menuRepository.findByYmd(date)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        return GetMenuDTO.mapToDTO(menu);
    }
}
