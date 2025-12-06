package com.cafe.order.domain.menu.dto;

public enum ShotOption {

    NONE("없음", 0),                  // 기본값 (Beverage/Dessert)
    BASIC("기본", 0),                 // 기본 2샷
    LIGHT("연하게", 0),               // 1샷
    EXTRA("샷 추가", 500),            // 샷 추가
    DECAFFEINATED("디카페인", 1000);  // 디카페인 옵션

    private final String displayName;
    private final int priceDelta;

    ShotOption(String displayName, int priceDelta) {
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
