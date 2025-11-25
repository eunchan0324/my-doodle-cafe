package com.cafe.order.domain.storemenu.dto;

public enum RecommendType {
    BEST("베스트 메뉴"),
    NEW("신메뉴"),
    SEASON("시즌 메뉴");

    private final String displayName;

    RecommendType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
