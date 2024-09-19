package GBFH.GBFH_BE.repository;

import GBFH.GBFH_BE.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    Optional<UserInfo> findByUserNo(String userNo);
}
