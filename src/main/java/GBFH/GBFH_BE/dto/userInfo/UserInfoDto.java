package GBFH.GBFH_BE.dto.userInfo;

import GBFH.GBFH_BE.entity.main.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserInfoDto {
    private String userNo;
    private String nameKor;
    private String hpNo;
    private String roomNo; //호수
    private String house;

    public static UserInfoDto toDto(UserInfo userInfo) {
        return UserInfoDto.builder()
                .userNo(userInfo.getUserNo())
                .nameKor(userInfo.getNameKor())
                .hpNo(userInfo.getHpNo())
                .roomNo(userInfo.getRoomNo())
                .house(userInfo.getHouse().name())
                .build();
    }
}
