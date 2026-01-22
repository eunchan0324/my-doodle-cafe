package com.cafe.order.domain.storemenu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerRecommendUpdateRequest {
    private RecommendType recommendType; // 수정할 추천 타입
}
