package com.cafe.order.domain.storemenu.service;

import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.menu.service.MenuService;
import com.cafe.order.domain.storemenu.dto.MenuWithAvailability;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import com.cafe.order.domain.storemenu.repo.JpaStoreMenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StoreMenuService {

    private final JpaStoreMenuRepository storeMenuRepository;


    private final MenuService menuService;

    public StoreMenuService(JpaStoreMenuRepository storeMenuRepository, MenuService menuService) {
        this.storeMenuRepository = storeMenuRepository;
        this.menuService = menuService;
    }

    /**
     * READ : 지점의 판매 메뉴 조회 (Menu 정보 포함)
     */
    public List<MenuWithAvailability> findStoreMenus(Integer storeId) {
        List<StoreMenu> storeMenus = storeMenuRepository.findByStoreId(storeId);

        return storeMenus.stream()
                .map(sm -> {
                    Menu menu = menuService.findById(sm.getMenuId());
                    return new MenuWithAvailability(
                            menu,
                            sm.getIsAvailable(),
                            sm.getRecommendType()
                    );
                })
                .collect(Collectors.toList());
    }



    /**
     * READ : 지점의 판매 메뉴 조회 (StoreMenu만)
     */
    public List<StoreMenu> findByStoreId(Integer storeId) {
        return storeMenuRepository.findByStoreId(storeId);
    }


    /**
     * UPDATE : 판매 메뉴 일괄 업데이트
     * - 기존 판매 메뉴 전체 삭제
     * - 새로 선택된 메뉴들만 추가
     */
    public void updateStoreMenus(Integer storeId, List<UUID> menuIds) {
        // 1. 기존 판매 메뉴 전체 삭제
        List<StoreMenu> existingMenus = storeMenuRepository.findByStoreId(storeId);
        for (StoreMenu sm : existingMenus) {
            storeMenuRepository.deleteById(sm.getId());
        }

        // 2. 새로 선택된 메뉴들 추가
        if (menuIds != null && !menuIds.isEmpty()) {
            for (UUID menuId : menuIds) {
                StoreMenu newStoreMenu = new StoreMenu(storeId, menuId);
                storeMenuRepository.save(newStoreMenu); // JPA용
            }
        }
    }



    // TODO : 개밸 메뉴 추가/삭제 (나중에 API에서 사용)

}
