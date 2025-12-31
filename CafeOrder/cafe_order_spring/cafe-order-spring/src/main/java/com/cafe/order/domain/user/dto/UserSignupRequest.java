package com.cafe.order.domain.user.dto;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String loginId;
    private String password;
    private String name;
}
