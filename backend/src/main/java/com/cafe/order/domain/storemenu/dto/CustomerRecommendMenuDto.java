package com.cafe.order.domain.storemenu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerRecommendMenuDto {

    private String menuName;
    private RecommendType recommendType;
}
