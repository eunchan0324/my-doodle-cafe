package com.cafe.order.domain.menu.dto;

public enum Temperature {
    ICE("ICE"),
    HOT("HOT");

    private final String displayName;

    Temperature(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

