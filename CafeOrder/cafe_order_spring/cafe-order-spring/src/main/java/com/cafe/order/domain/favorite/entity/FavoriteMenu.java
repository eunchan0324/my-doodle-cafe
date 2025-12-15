package com.cafe.order.domain.favorite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Table(name = "favorite_menu")
@Entity
public class FavoriteMenu {

    @EmbeddedId
    private FavoriteMenuId id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public FavoriteMenu(String cusotmerId, UUID menuId) {
        this.id = new FavoriteMenuId(cusotmerId, menuId);
        this.createdAt = LocalDateTime.now();
    }

    // DB 조회용 생성자
    public FavoriteMenu(FavoriteMenuId id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
