package com.cafe.order.domain.order.dto;

import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * 장바구니 항목 하나 추가 시 사용되는 [요청] DTO
 * <br>
 * 목적: 메뉴 상세 페이지에서 메뉴 항목 하나와 그 옵션을 서버에 전달하여 장바구니에 '추가'할 때 사용
 */
@Getter
@Setter
public class CustomerOrderItemRequest {

    // 1. 필수 식별자 및 수량
    private UUID menuId;
    private Integer quantity;

    // 2. 옵션 필드
    private Temperature temperature;
    private CupType cupType;
    private ShotOption shotOption;



}
