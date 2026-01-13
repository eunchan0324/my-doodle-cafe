package com.cafe.order.domain.user.entity;

public enum UserRole {
    ADMIN("관리자"),
    SELLER("판매자"),
    CUSTOMER("고객");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
