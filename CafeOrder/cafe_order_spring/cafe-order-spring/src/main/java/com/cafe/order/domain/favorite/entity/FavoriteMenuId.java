package com.cafe.order.domain.favorite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode // 식별자 클래스는 반드시 equals, hashCode 구현 필수
@Embeddable
public class FavoriteMenuId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "menu_id")
    private UUID menuId;
}
