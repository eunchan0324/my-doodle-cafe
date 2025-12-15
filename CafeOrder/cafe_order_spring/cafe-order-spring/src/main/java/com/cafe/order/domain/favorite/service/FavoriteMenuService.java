package com.cafe.order.domain.favorite.service;

import com.cafe.order.domain.favorite.dto.FavoriteMenuResponse;
import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import com.cafe.order.domain.favorite.repo.InMemoryFavoriteMenuRepository;
import com.cafe.order.domain.favorite.repo.JpaFavoriteMenuRepository;
import com.cafe.order.domain.favorite.repo.SqlFavoriteMenuRepository;
import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class FavoriteMenuService {

    private final JpaFavoriteMenuRepository favoriteMenuRepository;
//        private final SqlFavoriteMenuRepository favoriteMenuRepository;
//    private final InMemoryFavoriteMenuRepository favoriteMenuRepository;

    private final JpaMenuRepository menuRepository;

    public FavoriteMenuService(JpaFavoriteMenuRepository favoriteMenuRepository, JpaMenuRepository menuRepository) {
        this.favoriteMenuRepository = favoriteMenuRepository;
        this.menuRepository = menuRepository;
    }

    /**
     * READ: 특정 고객이 특정 메뉴를 찜했는지 여부를 조회 (메뉴 상세 화면용)
     */
    @Transactional(readOnly = true)
    public boolean isMenuFavorite(String customerId, UUID menuId) {
        return favoriteMenuRepository.existsById_CustomerIdAndId_MenuId(customerId, menuId);
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
     * READ : 고객의 찜 목록 전체 조회
     */
    public List<FavoriteMenuResponse> favoriteMenuList(String customerId) {
        // 1. 해당 고객이 찜한 FavoriteMenu 엔티티 목록 조회
        List<FavoriteMenu> favorites = favoriteMenuRepository.findById_CustomerId(customerId);

        // 서비스 메서드가 List<T> 타입을 반환하도록 선언되어 있다면, 일반적으로 절대 null을 반환하지 않는 것이 가장 좋은 관례
        // 추후 View 에서 .isEmpty() 로 해결
        if (favorites.isEmpty()) {
            return List.of();
        }

        // 2. 찜된 메뉴 ID 목록 추출
        List<UUID> menuIds = favorites.stream()
            .map(f -> f.getId().getMenuId())
            .collect(Collectors.toList());

        // 3. 메뉴 ID 목록으로 Menu 정보 일괄 조회 (N+1 문제 방지)
        // Map<UUID, Menu> 형태로 변환하여 쉽게 찾을 수 있도록 함
        Map<UUID, Menu> menuMap = menuRepository.findAllById(menuIds).stream()
            .collect(Collectors.toMap(Menu::getId, m -> m));

        // 4. FavoriteMenu와 Menu 정보를 조합하여 DTO로 변환
        return favorites.stream()
            .filter(f -> menuMap.containsKey(f.getId().getMenuId())) // 혹시 삭제된 메뉴는 필터링
            .map(f -> {
                Menu menu = menuMap.get(f.getId().getMenuId());
                return new FavoriteMenuResponse(
                    menu.getId(),
                    menu.getName(),
                    menu.getPrice(),
                    menu.getCategory(),
                    f.getCreatedAt()
                );
            })
            .collect(Collectors.toList());
    }
}
