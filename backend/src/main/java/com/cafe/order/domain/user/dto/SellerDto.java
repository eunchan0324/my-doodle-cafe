package com.cafe.order.domain.user.dto;

import com.cafe.order.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerDto {
    private Integer id;
    private String loginId;
    private String password;
    private String name;
    private Integer storeId;
    private String storeName;

    public SellerDto(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.name = user.getName();

        // Store 객체 null 체크 후 데이터 꺼내기
        if (user.getStore() != null) {
            this.storeId = user.getStore().getId();
            this.storeName = user.getStore().getName();
        } else {
            this.storeId = null;
            this.storeName = "지점없음"; // 기본값 설정
        }
    }
}
