package com.cafe.order.domain.order.dto;

import com.cafe.order.domain.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class SellerOrderResponse {

    private UUID orderId;
    private Integer waitingNumber;
    private OrderStatus status;
    private Integer totalPrice;
    private LocalDateTime orderTime;
    private List<SellerOrderItemDto> items; // 내부 아이템용 DTO

    public SellerOrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.waitingNumber = order.getWaitingNumber();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderTime = order.getOrderTime();
        this.items = order.getItems().stream()
                .map(SellerOrderItemDto::new)
                .toList();
    }
}
