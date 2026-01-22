package com.cafe.order.domain.storemenu.controller.api;

import com.cafe.order.domain.storemenu.dto.SellerMenuResponse;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
import com.cafe.order.domain.storemenu.service.StoreMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
