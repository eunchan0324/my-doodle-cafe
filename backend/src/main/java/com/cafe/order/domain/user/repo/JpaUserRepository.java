package com.cafe.order.domain.user.repo;

import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {

    // role 필터링
    List<User> findByRole(UserRole role);

    // loginId로 회원 정보를 찾는 메서드
    Optional<User> findByLoginId(String loginId);

}
