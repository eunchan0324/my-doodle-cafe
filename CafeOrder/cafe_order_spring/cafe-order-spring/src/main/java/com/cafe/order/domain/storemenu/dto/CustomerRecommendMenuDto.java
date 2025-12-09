package com.cafe.order.domain.storemenu.dto;

import lombok.Getter;

@Getter
public class CustomerRecommendMenuDto {

    private String menuName;
    private RecommendType recommendType;

    public CustomerRecommendMenuDto(String menuName, RecommendType recommendType) {
        this.menuName = menuName;
        this.recommendType = recommendType;
    }
}
