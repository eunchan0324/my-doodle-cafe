package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuWithRecommendType {
    private Menu menu;
    private RecommendType recommendType;
}
