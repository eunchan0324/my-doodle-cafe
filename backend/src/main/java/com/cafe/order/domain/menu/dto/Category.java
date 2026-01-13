package com.cafe.order.domain.menu.dto;

public enum Category {
    COFFEE("커피"),
    BEVERAGE("음료"),
    DESSERT("디저트");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
