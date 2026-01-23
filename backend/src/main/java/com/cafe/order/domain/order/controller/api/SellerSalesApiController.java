package com.cafe.order.domain.order.controller.api;

import com.cafe.order.domain.order.dto.SellerDailySalesResponse;
import com.cafe.order.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores/{storeId}/sales")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class SellerSalesApiController {

    private final OrderService orderService;

    /**
     * 오늘 매출 및 내역 조회
     */
    @GetMapping("/today")
    public ResponseEntity<SellerDailySalesResponse> getTodaySales(@PathVariable Integer storeId) {

        SellerDailySalesResponse response = orderService.getTodaySales(storeId);

        return ResponseEntity.ok(response);
    }
}
