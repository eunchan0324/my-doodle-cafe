package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menustatus.entity.SalesStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CustomerMenuResponse {

    private UUID menuId;
    private String name;
    private Integer price;
    private Category category;
    private SalesStatus status;
    public CustomerMenuResponse(UUID menuId, String name, Integer price, Category category, SalesStatus status) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.status = status;
    }
}
