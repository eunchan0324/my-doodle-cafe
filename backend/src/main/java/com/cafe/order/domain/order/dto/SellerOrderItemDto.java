package com.cafe.order.domain.order.dto;

import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import com.cafe.order.domain.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class SellerOrderItemDto {

    private String menuName;
    private Integer quantity;
    private Temperature temperature;
    private CupType cupType;
    private ShotOption shotOption;

    public SellerOrderItemDto(OrderItem item) {
        this.menuName = item.getMenuName();
        this.quantity = item.getQuantity();
        this.temperature = item.getTemperature();
        this.cupType = item.getCupType();
        this.shotOption = item.getOptions();
    }
}
