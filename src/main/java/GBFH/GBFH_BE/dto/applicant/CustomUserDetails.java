package GBFH.GBFH_BE.dto.applicant;

import GBFH.GBFH_BE.entity.main.Applicant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails  implements UserDetails {

    private final Applicant applicant;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        Collection<GrantedAuthority> collection = new ArrayList<>();
//
//        collection.add(new GrantedAuthority() {
//
//            @Override
//            public String getAuthority() {
//
//                return applicant.getRole();
//            }
//        });

//        return collection;

        // 권한 정보가 없어 빈 컬렉션 반환
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {

        return applicant.getLoginPwd();
    }

    @Override
    public String getUsername() {

        return applicant.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
