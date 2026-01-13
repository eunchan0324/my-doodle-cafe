package com.cafe.order.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SalesDto {
    private String storeName; // 지점명
    private Integer orderCount; // 주문 수
    private Integer totalSales; // 총 매출
}
