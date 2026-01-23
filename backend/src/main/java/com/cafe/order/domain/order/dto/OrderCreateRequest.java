package com.cafe.order.domain.order.dto;

import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OrderCreateRequest {

    private Integer storeId;
    private List<ItemRequest> items;

    @Getter
    public static class ItemRequest {
        private UUID menuId;
        private Integer quantity;
        private CupType cupType;
        private Temperature temperature;
        private ShotOption shotOption;
    }
}
