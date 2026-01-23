package com.cafe.order.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SellerDailySalesResponse {

    // 1. 요약 통계 (Summary)
    private Long totalSales; // 오늘 총 매출액
    private Integer totalCount; // 오늘 총 주문(결제) 건수

    // 2. 상세 내역 (Detail List)
    private List<SellerOrderResponse> orderHistory;
}
