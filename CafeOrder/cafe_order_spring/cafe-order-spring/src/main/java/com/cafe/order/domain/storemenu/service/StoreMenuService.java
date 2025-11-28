package com.cafe.order.domain.storemenu.service;

import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.menu.service.MenuService;
import com.cafe.order.domain.storemenu.dto.MenuWithAvailability;
import com.cafe.order.domain.storemenu.dto.MenuWithRecommendType;
import com.cafe.order.domain.storemenu.dto.RecommendType;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import com.cafe.order.domain.storemenu.repo.InMemoryStoreMenuRepository;
import com.cafe.order.domain.storemenu.repo.JpaStoreMenuRepository;
import com.cafe.order.domain.storemenu.repo.SqlStoreMenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StoreMenuService {

        private final JpaStoreMenuRepository storeMenuRepository;
//    private final SqlStoreMenuRepository storeMenuRepository;
//    private final InMemoryStoreMenuRepository storeMenuRepository;

    private final MenuService menuService;

    public StoreMenuService(JpaStoreMenuRepository storeMenuRepository, MenuService menuService) {
        this.storeMenuRepository = storeMenuRepository;
        this.menuService = menuService;
    }

    // 판매자 판매 메뉴 관리 기능
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
                storeMenuRepository.save(newStoreMenu);
            }
        }
    }


    // 판매자 메뉴 추천 기능
    /**
     * READ : 지점의 메뉴 + 추천 타입 조회
     */
    public List<MenuWithRecommendType> findStoreMenusWithRecommendType(Integer storeId) {
        // 1. StoreMenu 조회
        List<StoreMenu> storeMenus = storeMenuRepository.findByStoreId(storeId);

        // 2. 수동으로 Menu 조회 + DTO 조합
        return storeMenus.stream()
                .map(sm -> {
                    // menuId로 Menu 조회
                    Menu menu = menuService.findById(sm.getMenuId());

                    // DTO 생성
                    return new MenuWithRecommendType(menu, sm.getRecommendType());
                })
                .collect(Collectors.toList());
    }

    /**
     * UPDATE : 추천 타입 일괄 업데이트
     * - params에서 "recommendType_UUID" 형태로 전달된 데이터 파싱
     * - HTML Form에서 전달된 여러 메뉴의 추천 타입 변경 요청을
     * 하나씩 읽어들여, 해당 메뉴의 StoreMenu 데이터를 찾아 업데이트하는 함수
     */
    public void updateRecommendTypes(Integer storeId, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();

            if (key.startsWith("recommendType_")) {
                // UUID 추출
                String menuIdStr = key.replace("recommendType_", "");
                UUID menuId = UUID.fromString(menuIdStr);

                // RecommendType Enum 변환
                String recommendTypeStr = entry.getValue();
                RecommendType recommendType = RecommendType.valueOf(recommendTypeStr);

                // StoreMenu 조회 및 업데이트
                StoreMenu storeMenu = storeMenuRepository
                        .findByStoreIdAndMenuId(storeId, menuId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "StoreMenu not found: storeId=" + storeId + ", menuId=" + menuId
                        ));

                storeMenu.setRecommendType(recommendType);
                storeMenuRepository.save(storeMenu);
            }
        }
    }



    // TODO : 개별 메뉴 추가/삭제 (나중에 API에서 사용)

}
