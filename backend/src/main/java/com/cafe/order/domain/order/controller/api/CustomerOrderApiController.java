package com.cafe.order.domain.order.controller.api;

import com.cafe.order.domain.order.dto.OrderCreateRequest;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.service.OrderService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerOrderApiController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody OrderCreateRequest request) {

        // 1. 비로그인 처리
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 2. 서비스 호출
        Integer userId = userDetails.getId();
        Order order = orderService.createOrder(userId, request);

        // 3. 성공 응답 (생성된 주문 ID 반환)
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getOrderId());
        response.put("waitingNumber", order.getWaitingNumber());
        response.put("message", "주문이 성공적으로 접수되었습니다.");

        // 상태코드 201
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
