package com.cafe.order.domain.storemenu.service;

import com.cafe.order.domain.favorite.service.FavoriteMenuService;
import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.menu.service.MenuService;
import com.cafe.order.domain.menustatus.entity.MenuStatus;
import com.cafe.order.domain.menustatus.entity.MenuStatusId;
import com.cafe.order.domain.menustatus.repo.JpaSellerStockRepository;
import com.cafe.order.domain.menustatus.service.SellerStockService;
import com.cafe.order.domain.storemenu.dto.*;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
import com.cafe.order.domain.storemenu.repo.JpaStoreMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreMenuService {

    private final JpaStoreMenuRepository storeMenuRepository;
//    private final SqlStoreMenuRepository storeMenuRepository;
//    private final InMemoryStoreMenuRepository storeMenuRepository;

    private final MenuService menuService;
    private final JpaMenuRepository menuRepository;
    private final SellerStockService sellerStockService;
    private final JpaSellerStockRepository sellerStockRepository;
    private final FavoriteMenuService favoriteMenuService;

    /**
     * 판매자 : 판매 메뉴 관리 기능
     */

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
     * READ : 특정 지점에서 고객에게 보여줄 수 있는(판매 가능한) 메뉴 목록 조회
     */
    public List<CustomerMenuResponse> findSellableMenus(Integer storeId) {
        List<CustomerMenuResponse> result = new ArrayList<>();

        List<StoreMenu> storeMenus = storeMenuRepository.findByStoreId(storeId);

        for (StoreMenu sm : storeMenus) {
            if (!sm.getIsAvailable()) {
                continue;
            }

            var msId = new MenuStatusId(storeId, sm.getMenuId());
            MenuStatus ms = sellerStockRepository.findById(msId)
                .orElseThrow(() -> new IllegalStateException("MenuStatus not found for storeId=" + storeId + ", menuId=" + sm.getMenuId()));

            if (!ms.isSellable()) {
                continue;
            }

            Menu menu = menuRepository.findById(sm.getMenuId())
                .orElseThrow(() -> new IllegalStateException("Menu not found (menuId=" + sm.getMenuId() + ")"));


            var response = new CustomerMenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                ms.getMenu().getCategory(),
                ms.getStatus()
            );

            result.add(response);
        }
        return result;
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


    /**
     * 판매자 : 메뉴 추천 기능
     */

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


    /**
     * 구매자 기능
     */
    /**
     * READ : 구매자 추천 메뉴 Dto 조회
     * <br>
     * - storeId로 StoreMenu 엔티티 조회 <br>
     * - Storemenu의 menuId로 Menu 엔티티 조회 <br>
     * - DTO 결합하여 반환
     */
    public List<CustomerRecommendMenuDto> findRecommendMenus(Integer storeId) {
        List<CustomerRecommendMenuDto> result = new ArrayList<>();

        List<StoreMenu> storeMenus = storeMenuRepository.findByStoreId(storeId);

        for (StoreMenu sm : storeMenus) {
            Menu menu = menuService.findById(sm.getMenuId());
            result.add(new CustomerRecommendMenuDto(
                menu.getName(),
                sm.getRecommendType()
            ));
        }
        return result;
    }

    /**
     * READ : 지점의 전체 메뉴 조회 + 판매 가능 여부(재고 상태) 포함
     */
    public List<CustomerMenuResponse> findAllMenusWithAvailability(Integer storeId) {
        List<CustomerMenuResponse> result = new ArrayList<>();

        // 1. 해당 지점의 모든 storeMenu를 조회
        List<StoreMenu> storeMenus = storeMenuRepository.findByStoreId(storeId);

        for (StoreMenu sm : storeMenus) {
            // 2. MenuStatus 찾기
            var msId = new MenuStatusId(storeId, sm.getMenuId());
            MenuStatus ms = sellerStockRepository.findById(msId)
                .orElseThrow(() -> new IllegalStateException("MenuStatus not found for storeId=" + storeId + ", menuId=" + sm.getMenuId()));

            // 3. Menu 찾기
            Menu menu = menuRepository.findById(sm.getMenuId())
                .orElseThrow(() -> new IllegalStateException("Menu not found (menuId=" + sm.getMenuId() + ")"));

            // 4. DTO 생성
            var response = new CustomerMenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getCategory(),
                sm.getRecommendType(),
                ms.getStatus()
            );
            result.add(response);
        }
        return result;
    }

    /**
     * READ : 메뉴 상세 조회
     */
    public CustomerMenuDetailResponse findMenuDetail(Integer storeId, UUID menuId, String customerId) {
        // 1. Menu 정보 조회
        Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다. menuId : " + menuId));

        // 2. StoreMenu 조회
        StoreMenu storeMenu = storeMenuRepository.findByStoreIdAndMenuId(storeId, menuId)
            .orElseThrow(() -> new IllegalArgumentException("StoreMenu를 찾을 수 없습니다. storeId : " + storeId + ", menuId : " + menuId));

        // 3. MenuStatus 조회
        MenuStatusId msId = new MenuStatusId(storeId, menuId);
        MenuStatus ms = sellerStockRepository.findById(msId)
            .orElseThrow(() -> new IllegalStateException("해당 지점의 메뉴 상태를 찾을 수 없습니다."));

        // 4. 찜 상태 확인 (MyMenuRepository.findByUserIdAndMenuId 호출 예정)
        boolean isFavorite = favoriteMenuService.isMenuFavorite(customerId, menuId);

        // 5. DTO 조합 및 반환
        return new CustomerMenuDetailResponse(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getDescription(),
            storeMenu.getRecommendType(),
            ms.getStatus(),
            isFavorite,
            menu.getCategory()
        );
    }

}
