package com.cafe.order.domain.menu.dto;


public enum SalesStatus {
    READY("준비중"),
    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    STOP("판매중지");

    private final String displayName;

    SalesStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
