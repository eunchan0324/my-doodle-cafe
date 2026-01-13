package com.cafe.order.domain.favorite.service;

import com.cafe.order.domain.favorite.dto.FavoriteMenuResponse;
import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import com.cafe.order.domain.favorite.repo.JpaFavoriteMenuRepository;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.repo.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class FavoriteMenuService {

    private final JpaFavoriteMenuRepository favoriteMenuRepository;
//        private final SqlFavoriteMenuRepository favoriteMenuRepository;
//    private final InMemoryFavoriteMenuRepository favoriteMenuRepository;

    private final JpaMenuRepository menuRepository;
    private final JpaUserRepository userRepository;

    /**
     * READ: 특정 고객이 특정 메뉴를 찜했는지 여부를 조회 (메뉴 상세 화면용)
     */
    @Transactional(readOnly = true)
    public boolean isMenuFavorite(Integer userId, UUID menuId) {
        return favoriteMenuRepository.existsByUser_IdAndMenu_Id(userId, menuId);
    }

    /**
     * COMMAND: 찜 상태를 토글 (찜이 되어 있으면 해제, 아니면 등록)
     */
    public void toggleFavorite(Integer userId, UUID menuId) {
        // 1. 복합 키 객체 생성
        FavoriteMenuId favoriteMenuId = new FavoriteMenuId(userId, menuId);

        // 2. 현재 찜 상태 확인 토글
        if (favoriteMenuRepository.existsById(favoriteMenuId)) {
            // 2-1. 찜이 되어있으면 해제 (DELETE)
            favoriteMenuRepository.deleteById(favoriteMenuId);
        } else {
            // 2-2. 찜이 안되어있으면 등록 (INSERT)
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

            Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

            // 생성자 : new FavoriteMenu(User, Menu)
            FavoriteMenu newFavoriteMenu = new FavoriteMenu(user, menu);
            favoriteMenuRepository.save(newFavoriteMenu);
        }
    }

    /**
     * READ : 고객의 찜 목록 전체 조회
     */
    public List<FavoriteMenuResponse> favoriteMenuList(Integer userId) {
        // 1. 해당 고객이 찜한 FavoriteMenu 엔티티 목록 조회
        List<FavoriteMenu> favorites = favoriteMenuRepository.findByUser_Id(userId);

        if (favorites.isEmpty()) {
            return List.of();
        }

        // 2. DTO 변환
        return favorites.stream()
            .map(f -> {
                Menu menu = f.getMenu();

                return new FavoriteMenuResponse(
                    menu.getId(),
                    menu.getName(),
                    menu.getPrice(),
                    menu.getCategory(),
                    f.getCreatedAt()
                );
            })
            .toList();
    }
}
