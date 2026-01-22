package com.cafe.order.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusUpdateRequest {
    private OrderStatus orderStatus; // 변경할 상태 (PREPARING, READY, COMPLETED 등)
}
