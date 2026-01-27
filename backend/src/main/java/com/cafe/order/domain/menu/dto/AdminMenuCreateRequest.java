package com.cafe.order.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminMenuCreateRequest {

    private String name;
    private Integer price;
    private Category category;
    private String description;
}
