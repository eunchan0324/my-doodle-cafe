package com.cafe.order.domain.favorite.repo;

import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaFavoriteMenuRepository extends JpaRepository<FavoriteMenu, FavoriteMenuId> {

    // 특정 고객의 찜 목록 전체를 조회
    // findBy + Id_CustomerId (키 객체 이름 + 내부 필드 이름)
    List<FavoriteMenu> findById_CustomerId(String customerId);

    // 찜 존재 여부 확인
    // existsBy + Id_CustomerIdAndId_MenuId (두 필드 모두 키 객체 내부 필드임을 명시)
    boolean existsById_CustomerIdAndId_MenuId(String customerId, UUID menuId);
}
