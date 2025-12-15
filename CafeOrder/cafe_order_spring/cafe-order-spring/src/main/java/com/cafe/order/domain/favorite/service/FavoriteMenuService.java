package com.cafe.order.domain.favorite.service;

import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import com.cafe.order.domain.favorite.repo.JpaFavoriteMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class FavoriteMenuService {

    private final JpaFavoriteMenuRepository favoriteMenuRepository;

    /**
     * READ: 특정 고객이 특정 메뉴를 찜했는지 여부를 조회 (메뉴 상세 화면용)
     */
    @Transactional(readOnly = true)
    public boolean isMenuFavorite(String customerId, UUID menuId) {
        return favoriteMenuRepository.existsByCustomerIdAndMenuId(customerId, menuId);
    }

    /**
     * COMMAND: 찜 상태를 토글 (찜이 되어 있으면 해제, 아니면 등록)
     */
    public void toggleFavorite(String customerId, UUID menuId) {
        // 1. 복합 키 객체 생성
        FavoriteMenuId favoriteMenuId = new FavoriteMenuId(customerId, menuId);

        // 2. 현재 찜 상태 확인 토글
        if (favoriteMenuRepository.existsById(favoriteMenuId)) {
            // 2-1. 찜이 되어있으면 해제 (DELETE)
            favoriteMenuRepository.deleteById(favoriteMenuId);
        } else {
            // 2-2. 찜이 안되어있으면 등록 (INSERT)
            FavoriteMenu newFavoriteMenu = new FavoriteMenu(customerId, menuId);
            favoriteMenuRepository.save(newFavoriteMenu);
        }
    }

    /**
     * READ : 찜 목록 조회 메서드
     */
    public List<FavoriteMenu> favoriteMenuList(String customerId) {
        return favoriteMenuRepository.findByCustomerId(customerId);
    }
}
