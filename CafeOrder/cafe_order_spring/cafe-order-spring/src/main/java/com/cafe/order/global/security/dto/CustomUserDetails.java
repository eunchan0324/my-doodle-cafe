package com.cafe.order.global.security.dto;

import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티가 사용할 "인증된 사용자 정보" 객체
@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        // 우리 Role Enum 이름을 시큐리티가 좋아하는 "ROLE_" 접두사 붙여서 등록
        // 예: ADMIN -> ROLE_ADMIN
        collection.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId(); // 시큐리티의 username은 우리에겐 loginId
    }

    // 계정 만료/잠금 여부 등 (지금은 다 true로 설정해서 통과시킴)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Integer getId() {
        return user.getId();
    }

    /**
     * 로그인한 유저의 Store 정보 가져오기
     * (판매자가 아니면 null일 수 있음)
     */
    public Store getStore() {
        return user.getStore();
    }
}
