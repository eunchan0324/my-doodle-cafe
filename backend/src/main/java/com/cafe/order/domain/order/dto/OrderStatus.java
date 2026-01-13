package com.cafe.order.domain.order.dto;

public enum OrderStatus {
    ORDER_PLACED("주문 접수"),
    PREPARING("준비중"),
    READY("준비완료"),
    COMPLETED("픽업완료");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;}

}
