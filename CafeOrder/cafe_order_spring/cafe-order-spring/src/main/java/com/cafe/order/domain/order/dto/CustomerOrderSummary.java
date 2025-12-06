package com.cafe.order.domain.order.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerOrderSummary {

    private Integer watingNumber;
    private LocalDateTime orderTime;
    private Integer totalPrice;
    private OrderStatus orderStatus;
    private String menuSummary; // 메뉴 요약 : Order 엔티티 메서드 호출

    public CustomerOrderSummary(Order order) {
        this.watingNumber = order.getWaitingNumber();
        this.orderTime = order.getOrderTime();
        this.totalPrice = order.getTotalPrice();
        this.orderStatus = order.getStatus();
        this.menuSummary = order.getMenuSummary();
    }
}
