package com.cafe.order.domain.order.controller.api;

import com.cafe.order.domain.order.dto.OrderStatusUpdateRequest;
import com.cafe.order.domain.order.dto.SellerOrderItemDto;
import com.cafe.order.domain.order.dto.SellerOrderResponse;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.service.OrderService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stores/{storeId}/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class SellerOrderApiController {

    private final OrderService orderService;

    /**
     * 주문 대시보드 데이터 (목록 + 상세정보 포함)
     * - 완료된 주문을 제외한, 현재 진행중인 주문을 가져옴
     * - FE에서 상태별로 분류해서 보여줄 예정
     */
    @GetMapping
    public ResponseEntity<List<SellerOrderResponse>> getActiveOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer storeId) {

        List<Order> activeOrders = orderService.findActiveOrderByStoreId(storeId);

        List<SellerOrderResponse> response = activeOrders.stream()
                .map(SellerOrderResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * 주문 상태 변경
     */
    @PostMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer storeId,
            @PathVariable UUID orderId,
            @RequestBody OrderStatusUpdateRequest request) {

        orderService.updateStatus(orderId, request.getOrderStatus());

        return ResponseEntity.ok().body("주문 상태가 변경되었습니다.");
    }
}
