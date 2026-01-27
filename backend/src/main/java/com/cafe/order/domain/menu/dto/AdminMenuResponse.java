package com.cafe.order.domain.menu.dto;

import com.cafe.order.domain.menu.entity.Menu;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AdminMenuResponse {
    private final UUID id;
    private final String name;
    private final Integer price;
    private final Category category;
    private final String description;

    public AdminMenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.category = menu.getCategory();
        this.description = menu.getDescription();
    }
}