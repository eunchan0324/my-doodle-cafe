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
    List<FavoriteMenu> findByCustomerId(String customerId);

    // 찜 존재 여부 확인
    boolean existsByCustomerIdAndMenuId(String customerId, UUID menuId);
}
