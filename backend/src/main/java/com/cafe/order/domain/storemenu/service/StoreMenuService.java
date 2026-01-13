package com.cafe.order.domain.storemenu.service;

import com.cafe.order.domain.favorite.service.FavoriteMenuService;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.menu.service.MenuService;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.repo.JpaStoreRepository;
import com.cafe.order.domain.storemenu.dto.*;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
import com.cafe.order.domain.storemenu.repo.JpaStoreMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final JpaMenuRepository menuRepository;
    private final FavoriteMenuService favoriteMenuService;
    private final JpaStoreRepository storeRepository;

    // ========== 판매자 : 판매 메뉴 관리 기능 ==========

    /**
     * READ : 지점의 판매 메뉴 조회 (Menu 정보 + 판매 상태 포함)
     * 용도 : 판매자 관리 페이지 (리스트 출력용)
     */
    public List<MenuWithAvailability> findStoreMenus(Integer storeId) {
        // 지점 메뉴 조회
        List<StoreMenu> storeMenus = storeMenuRepository.findByStore_Id(storeId);

        return storeMenus.stream()
                .map(sm -> new MenuWithAvailability(
                                sm.getMenu(),
                                sm.getSalesStatus() == SalesStatus.ON_SALE,
                                sm.getRecommendType()
                        )
                )
                .collect(Collectors.toList());
    }

    /**
     * READ : 지점의 판매 메뉴 조회 (StoreMenu만)
     */
    public List<StoreMenu> findByStoreId(Integer storeId) {
        return storeMenuRepository.findByStore_Id(storeId);
    }

    /**
     * READ : 특정 지점에서 고객에게 보여줄 수 있는(판매 가능한) 메뉴 목록 조회
     */
    public List<CustomerMenuResponse> findSellableMenus(Integer storeId) {
        List<CustomerMenuResponse> result = new ArrayList<>();

        List<StoreMenu> storeMenus = storeMenuRepository.findByStore_Id(storeId);

        for (StoreMenu sm : storeMenus) {
            if (!sm.isSellable()) {
                continue;
            }

            var response = new CustomerMenuResponse(
                    sm.getMenu().getId(),
                    sm.getMenu().getName(),
                    sm.getMenu().getPrice(),
                    sm.getMenu().getCategory(),
                    sm.getSalesStatus()
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
    @Transactional
    public void updateStoreMenus(Integer storeId, List<UUID> selectedMenuIds) {
        // 1. Store 객체 조회 (StoreMenu 생성을 위해 필요)
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("지점을 찾을 수 없습니다. storeId=" + storeId));

        // Null 방어 (선택된 게 하나도 없으면 빈 리스트로 처리)
        List<UUID> safeSelectedIds = (selectedMenuIds != null) ? selectedMenuIds : new ArrayList<>();

        // 2. 현재 DB에 저장된 우리 지점 메뉴들 조회
        List<StoreMenu> currentMenus = storeMenuRepository.findByStore_Id(storeId);

        // [삭제 로직] : DB에는 있는데, 요청 목록(selectedMenuIds)에는 없는 것
        List<StoreMenu> toDelete = currentMenus.stream()
                .filter(sm -> !safeSelectedIds.contains(sm.getMenu().getId()))
                .toList();

        // 체크 해제된 메뉴들은 삭제 (재고 정보도 같이 사라짐에 주의)
        storeMenuRepository.deleteAll(toDelete);

        // [추가/유지 로직] : 요청 목록에 있는 것들 처리

        // 빠른 비교를 위해 현재 DB에 있는 메뉴 ID들을 추출
        List<UUID> existingMenuIds = currentMenus.stream()
                .map(sm -> sm.getMenu().getId())
                .toList();

        for (UUID menuId : safeSelectedIds) {
            // Case 1 : 이미 DB에 있는 메뉴라면? > 건드리지 않음 (재고/상태 보존)
            if (existingMenuIds.contains(menuId)) {
                continue;
            }

            // Case 2 : DB에 없던 새로운 메뉴라면? > 신규 생성
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다. menuId=" + menuId));

            // 생성자 : Store, Menu, stock=0, status=READY, recommend=NONE
            var newStoreMenu = new StoreMenu(
                    store,
                    menu,
                    0,
                    SalesStatus.READY, // 초기 상태는 준비중
                    null
            );

            storeMenuRepository.save(newStoreMenu);
        }
    }


    // ========== 판매자 : 판매 메뉴 추천 관련 ==========

    /**
     * READ : 지점의 메뉴 + 추천 타입 조회
     */
    public List<MenuWithRecommendType> findStoreMenusWithRecommendType(Integer storeId) {
        // 1. StoreMenu 조회
        List<StoreMenu> storeMenus = storeMenuRepository.findByStore_Id(storeId);

        // 2. DTO 조합
        return storeMenus.stream()
                .map(sm -> new MenuWithRecommendType(sm.getMenu(), sm.getRecommendType()))
                .collect(Collectors.toList());
    }

    /**
     * UPDATE : 추천 타입 일괄 업데이트
     * - params에서 "recommendType_UUID" 형태로 전달된 데이터 파싱
     * - HTML Form에서 전달된 여러 메뉴의 추천 타입 변경 요청을
     * 하나씩 읽어들여, 해당 메뉴의 StoreMenu 데이터를 찾아 업데이트하는 함수
     */
    @Transactional
    public void updateRecommendTypes(Integer storeId, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();

            if (key.startsWith("recommendType_")) {
                String menuIdStr = key.replace("recommendType_", "");
                UUID menuId = UUID.fromString(menuIdStr);
                RecommendType recommendType = RecommendType.valueOf(entry.getValue());

                // StoreMenu 조회 및 업데이트
                StoreMenu storeMenu = storeMenuRepository
                        .findByStore_IdAndMenu_Id(storeId, menuId)
                        .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

                storeMenu.setRecommendType(recommendType);
            }
        }
    }


    // ========== 판매자 : 재고 관련 ==========

    /**
     * UPDATE : 재고/판매 상태 수정
     */
    @Transactional
    public void updateStockAndStatus(Integer storeId, UUID menuId, int newStock, SalesStatus newStatus) {
        // 1 엔티티 조회
        StoreMenu sm = storeMenuRepository.findByStore_IdAndMenu_Id(storeId, menuId)
            .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        // 2. 재고 변경 로직
        int currentStock = sm.getStock();
        if (newStock > currentStock) {
            sm.increaseStock(newStock - currentStock);
        } else if (newStock < currentStock) {
            sm.decreaseStock(currentStock - newStock);
        }
        // newStock == currentStock이면 재고 변경 없음

        // 3. 상태 변경 로직
        if (newStatus == SalesStatus.STOP) {
            sm.stopSelling();
        } else if (newStatus == SalesStatus.ON_SALE) {
            // 현재 STOP 상태였다면 RESUME 호출
            if (sm.getSalesStatus() == SalesStatus.STOP) {
                sm.resumeSelling();
            }
            // 그 외(SOLD_OUT 등)는 increaseStock/decreaseStock에 의해 이미 자동 처리됨
        }

    }

    // TODO : 개별 메뉴 추가/삭제 (나중에 API에서 사용)


    // ========== 구매자 기능 ==========

    /**
     * READ : 구매자 추천 메뉴 Dto 조회
     * <br>
     * - storeId로 StoreMenu 엔티티 조회 <br>
     * - Storemenu의 menuId로 Menu 엔티티 조회 <br>
     * - DTO 결합하여 반환
     */
    public List<CustomerRecommendMenuDto> findRecommendMenus(Integer storeId) {
        return storeMenuRepository.findByStore_Id(storeId).stream()
                .map(sm -> new CustomerRecommendMenuDto(
                        sm.getMenu().getName(),
                        sm.getRecommendType()
                ))
                .toList();
    }

    /**
     * READ : 지점의 전체 메뉴 조회 + 판매 가능 여부(재고 상태) 포함
     */
    public List<CustomerMenuResponse> findAllMenusWithAvailability(Integer storeId) {
        return storeMenuRepository.findByStore_Id(storeId).stream()
                .map(sm -> new CustomerMenuResponse(
                        sm.getMenu().getId(),
                        sm.getMenu().getName(),
                        sm.getMenu().getPrice(),
                        sm.getMenu().getCategory(),
                        sm.getRecommendType(),
                        sm.getSalesStatus()
                ))
                .toList();
    }

    /**
     * READ : 메뉴 상세 조회
     */
    public CustomerMenuDetailResponse findMenuDetail(Integer storeId, UUID menuId, Integer userId) {
        // StoreMenu 조회
        StoreMenu storeMenu = storeMenuRepository.findByStore_IdAndMenu_Id(storeId, menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지점에서 판매하지 않는 메뉴입니다."));

        // 찜 상태 조회 (MyMenuRepository.findByUserIdAndMenuId 호출 예정)
        boolean isFavorite = favoriteMenuService.isMenuFavorite(userId, menuId);

        Menu menu = storeMenu.getMenu();

        // DTO 조합 및 반환
        return new CustomerMenuDetailResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getDescription(),
                storeMenu.getRecommendType(),
                storeMenu.getSalesStatus(),
                isFavorite,
                menu.getCategory()
        );
    }
}
