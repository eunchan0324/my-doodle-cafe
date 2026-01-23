package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.storemenu.entity.SalesStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerMenuUpdateRequest {
    private int stock; // 수정할 재료 수량
    private SalesStatus status; // 수정할 판매 상태
}
