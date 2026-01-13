package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CustomerMenuDetailResponse {

    private UUID menuId;
    private String name;
    private Integer price;
    private String description;
    private RecommendType recommendType;
    private SalesStatus status;
    private boolean isFavorite;
    private Category category;

    public CustomerMenuDetailResponse(UUID menuId, String name, Integer price, String description, RecommendType recommendType, SalesStatus status, boolean isFavorite, Category category) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.recommendType = recommendType;
        this.status = status;
        this.isFavorite = isFavorite;
        this.category = category;
    }
}
