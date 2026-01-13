package com.cafe.order.domain.menu.dto;

public enum CupType {
    DISPOSABLE("일회용컵", 0),
    STORE("매장컵", 0),
    PERSONAL("개인컵", -300); // 에코 할인 적용 가능

    private final String displayName;
    private final int priceDelta;

    CupType(String displayName, int priceDelta) {
        this.displayName = displayName;
        this.priceDelta = priceDelta;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriceDelta() {
        return priceDelta;
    }
}

