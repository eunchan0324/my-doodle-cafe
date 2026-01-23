package com.cafe.order.domain.storemenu.controller.api;

import com.cafe.order.domain.favorite.service.FavoriteMenuService;
import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.dto.CustomerMenuDetailResponse;
import com.cafe.order.domain.storemenu.dto.CustomerMenuResponse;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stores/{storeId}/menus")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class StoreMenuApiController {

    private final StoreService storeService;
    private final StoreMenuService storeMenuService;
    private final FavoriteMenuService favoriteMenuService;

    /**
     * 지점의 메뉴 리스트 조회
     */
    @GetMapping
    public ResponseEntity<?> getStoreMenus(@PathVariable Integer storeId) {

        // 1. 파라미터로 받은 storeId로 서비스에서 데이터 조회
        Store store = storeService.findById(storeId);
        List<CustomerMenuResponse> rawMenus = storeMenuService.findAllMenusWithAvailability(storeId);

        // 카테고리별 그룹핑
        Map<Category, List<CustomerMenuResponse>> groupMenus = rawMenus.stream()
                .collect(Collectors.groupingBy(CustomerMenuResponse::getCategory));

        // 4. 응답 데이터 구성 (Map으로 감싸서 지점 정보와 메뉴 정보를 함께 전달)
        Map<String, Object> response = new HashMap<>();
        response.put("store", store); // 지점 정보
        response.put("menus", groupMenus); // 메뉴 목록

        return ResponseEntity.ok(response);
    }

    /**
     * 메뉴 상세보기
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<?> getMenuDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer storeId,
            @PathVariable UUID menuId) {

        // 비로그인 처리
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        try {
            Integer userId = userDetails.getId();
            CustomerMenuDetailResponse menuDetailResponse = storeMenuService.findMenuDetail(storeId, menuId, userId);

            return ResponseEntity.ok(menuDetailResponse);
        } catch (IllegalArgumentException e) {
            // 예외 처리 (404)
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * 찜 상태 토글
     */
    @PostMapping("/{menuId}/toggle-favorite")
    public ResponseEntity<?> toggleFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer storeId,
            @PathVariable UUID menuId) {

        // 1. 비로그인 처리
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        Integer userId = userDetails.getId();

        // 2. 서비스 호출 (토글 실행)
        favoriteMenuService.toggleFavorite(userId, menuId);

        // 3. [팁] 현재 찜 상태가 어떤지 다시 확인해서 알려주기
        boolean isFavorite = favoriteMenuService.isMenuFavorite(userId, menuId);

        Map<String, Object> response = new HashMap<>();
        response.put("isFavorite", isFavorite);
        response.put("message", isFavorite ? "찜 목록에 추가되었습니다." : "찜 목록에서 제거되었습니다.");

        return ResponseEntity.ok(response);
    }
}
