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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customer/menus")
@RequiredArgsConstructor
public class ApiCustomerMenuController {

    private final StoreService storeService;
    private final StoreMenuService storeMenuService;
    private final FavoriteMenuService favoriteMenuService;

    /**
     * 지점의 메뉴 리스트 조회
     */
    @GetMapping
    public ResponseEntity<?> getStoreMenus(HttpSession session) {
        // 1. 세션에서 storeId 가져오기
        Integer storeId = (Integer) session.getAttribute("currentStoreId");

        // 2. storeId가 없을 경우 에러 응답 반환
        if (storeId == null) {
            return ResponseEntity.badRequest().body("지점이 선택되지 않았습니다.");
        }

        // 3. 서비스에서 데이터 조회
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
            HttpSession session,
            @PathVariable UUID menuId) {

        // 비로그인 처리
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        Integer storeId = (Integer) session.getAttribute("currentStoreId");

        // 지점 미선택 처리
        if (storeId == null) {
            return ResponseEntity.badRequest().body("지점이 선택되지 않았습니다.");
        }

        try {
            // 서비스 호출
            Integer userId = userDetails.getId();
            CustomerMenuDetailResponse menuDetailResponse = storeMenuService.findMenuDetail(storeId, menuId, userId);

            // 성공 응답
            return ResponseEntity.ok(menuDetailResponse);

        } catch (IllegalArgumentException e) {
            // 예외 처리 (404)
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
