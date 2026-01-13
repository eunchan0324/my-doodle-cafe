package com.cafe.order.domain.menu.repo;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

// Menu 엔티티를 UUID 키로 관리할 수 있는 CRUD 기능을 가진 저장소를 의미
@Repository
public interface JpaMenuRepository extends JpaRepository<Menu, UUID> {

    // 이름으로 메뉴 찾기
    List<Menu> findByName(String name);

    // 카테고리별 조회
    List<Menu> findByCategory(Category category);
}