package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.menu.dto.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SellerMenuManageDto {
    private UUID menuId;
    private String name;
    private int price;
    private Category category;
    private boolean isSelling; // 우리 가게에서 팔고있는지
}
