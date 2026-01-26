package com.cafe.order.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerUpdateRequest {
    private String password;
    private String name;
    private Integer storeId;
}
