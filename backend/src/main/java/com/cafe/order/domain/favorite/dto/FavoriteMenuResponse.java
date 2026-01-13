package com.cafe.order.domain.favorite.dto;

import com.cafe.order.domain.menu.dto.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class FavoriteMenuResponse {

    // 메뉴 정보
    private final UUID menuId;
    private final String menuName;
    private final int price;
    private final Category category;

    // 찜 정보
    private final LocalDateTime createdAt;
}
