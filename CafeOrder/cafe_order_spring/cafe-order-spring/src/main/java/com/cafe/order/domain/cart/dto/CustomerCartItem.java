package com.cafe.order.domain.cart.dto;

import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCartItem {

    // 1. 주문 항목 식별자 및 이름 (DB조회 정보)
    private UUID menuId;
    private String name;

    // 2. 수량 및 최종 가격 (서버 계산 정보)
    private Integer quantity;
    private Integer finalPrice; // 옵션 가격이 반영된 최종 가격

    // 3. 옵션 정보 (Request DTO에서 가져옴)
    private Temperature temperature;
    private CupType cupType;
    private ShotOption shotOption;
}
