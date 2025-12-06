package com.cafe.order.domain.order.dto;

public enum OrderType {
    HERE("매장"),
    TOGO("포장");

    private final String displayName;

    OrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
