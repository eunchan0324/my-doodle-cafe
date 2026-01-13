package com.cafe.order.domain.favorite.repo;

import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class InMemoryFavoriteMenuRepository {

    private final List<FavoriteMenu> favoriteMenus = new ArrayList<>();

    // 생성자 : 초기 데이터 없음
    public InMemoryFavoriteMenuRepository() {
    }

    /**
     * READ : 고객의 찜 목록 전체 조회 (stream)
     */
    public List<FavoriteMenu> findById_CustomerId(Integer userId) {
        return favoriteMenus.stream()
            .filter(f -> f.getId().getUserId().equals(userId))
            .collect(Collectors.toList());
    }

    /**
     * READ : 고객의 찜 목록 전체 조회 (자바 반복문)
     */
    @Deprecated
    public List<FavoriteMenu> findById_CustomerIdBasedRoop(Integer userId) {
        List<FavoriteMenu> result = new ArrayList<>();

        for (FavoriteMenu favoriteMenu : favoriteMenus) {
            if (favoriteMenu.getUser().getId().equals(userId)) {
                result.add(favoriteMenu);
            }
        }

        return result;
    }


    /**
     * READ : 특정 고객이 특정 메뉴를 찜했는지 여부를 조회 (메뉴 상세 화면용)
     */
    public boolean existsById_CustomerIdAndId_MenuId(Integer userId, UUID menuId) {
        for (FavoriteMenu favoriteMenu : favoriteMenus) {
            if (favoriteMenu.getUser().getId().equals(userId)
                    && favoriteMenu.getId().getMenuId().equals(menuId)) {
                return true;
            }
        }
        return false;
    }


    /**
     * READ : 복합 키 객체(favoriteMenuId)를 받아서 존재 여부 확인 (람다식)
     */
    public boolean existsById(FavoriteMenuId id) {
        return favoriteMenus.stream()
                .anyMatch(f -> f.getId().equals(id));
    }

    /**
     * READ : 복합 키 객체(favoriteMenuId)를 받아서 존재 여부 확인 (자바 반복문)
     */
    @Deprecated
    public boolean existsByIdBasedRoop(FavoriteMenuId id) {
        for (FavoriteMenu favoriteMenu : favoriteMenus) {
            if (favoriteMenu.getUser().getId().equals(id.getUserId())
                    && favoriteMenu.getId().getMenuId().equals(id.getMenuId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * DELETE : 찜 해제
     */
    public void deleteById(FavoriteMenuId id) {
        favoriteMenus.removeIf(f -> f.getId().equals(id));
    }

    /**
     * CREATE : 찜 등록
     */
    public void save(FavoriteMenu favoriteMenu) {
        favoriteMenus.add(favoriteMenu);
    }

}
