package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuWithAvailability {
    private Menu menu;
    private Boolean isAvailable;
    private RecommendType recommendType;

    // 추천 여부 확인 헬퍼 메서드
    public boolean isRecommended() {
        return recommendType != null;
    }

    // 추천 타입 표시명
    public String getRecommendDisplayName() {
        return recommendType != null ? recommendType.getDisplayName() : "";
    }
}
