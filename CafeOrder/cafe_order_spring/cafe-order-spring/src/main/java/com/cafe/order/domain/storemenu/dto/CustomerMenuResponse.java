package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CustomerMenuResponse {

    private UUID menuId;
    private String name;
    private Integer price;
    private Category category;
    private RecommendType recommendType;
    private SalesStatus status;

    public CustomerMenuResponse(UUID menuId, String name, Integer price, Category category, RecommendType recommendType, SalesStatus status) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.recommendType = recommendType;
        this.status = status;
    }

    public CustomerMenuResponse(UUID menuId, String name, Integer price, Category category, SalesStatus status) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.status = status;
    }
}
