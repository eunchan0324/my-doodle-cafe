package com.cafe.order.domain.storemenu.controller.api;

import com.cafe.order.domain.storemenu.dto.SellerMenuResponse;
import com.cafe.order.domain.storemenu.dto.SellerMenuUpdateRequest;
import com.cafe.order.domain.storemenu.dto.SellerRecommendUpdateRequest;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stores/{storeId}/menus")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class SellerMenuApiController {

    private final StoreMenuService storeMenuService;

    /**
     * 판매 현황 목록 조회
     * - 현재 우리 가게에서 판매하기로 등록된 메뉴만 조회
     * - 재고, 상태, 추천 여부가 포함된 SellerMenuResponse 리스트를 반환
     */
    @GetMapping
    public ResponseEntity<List<SellerMenuResponse>> getStoreMenus(@PathVariable Integer storeId) {

        List<StoreMenu> storeMenus = storeMenuService.findByStoreId(storeId);

        List<SellerMenuResponse> response = storeMenus.stream()
                .map(SellerMenuResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * 재고, 판매 상태 수정
     */
    @PatchMapping("/{menuId}/update")
    public ResponseEntity<?> updateStockStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer storeId,
            @PathVariable UUID menuId,
            @RequestBody SellerMenuUpdateRequest request) {

        storeMenuService.updateStockAndStatus(storeId, menuId, request.getStock(), request.getStatus());

        return ResponseEntity.ok().body("재고, 판매 상태가 변경되었습니다.");
    }

    /**
     * 추천 타입 수정
     */
    @PatchMapping("/{menuId}/recommend")
    public ResponseEntity<?> updateRecommend(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer storeId,
            @PathVariable UUID menuId,
            @RequestBody SellerRecommendUpdateRequest request) {

        storeMenuService.updateRecommendType(storeId, menuId, request.getRecommendType());

        return ResponseEntity.ok().body("추천 상태가 변경되었습니다.");
    }

}
