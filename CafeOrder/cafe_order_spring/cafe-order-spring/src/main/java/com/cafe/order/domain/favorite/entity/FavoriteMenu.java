package com.cafe.order.domain.favorite.entity;

import com.cafe.order.common.entity.BaseEntity;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorite_menu")
@Entity
public class FavoriteMenu extends BaseEntity {

    @EmbeddedId
    private FavoriteMenuId id;

    @MapsId("userId") // FavoriteMenuId.userId와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("menuId") // FavoriteMenuId.menuId와 매핑됨
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 생성자
    public FavoriteMenu(User user, Menu menu) {
        this.user = user;
        this.menu = menu;
        // PK 객체도 생성해서 넣어줘야 영속성 컨텍스트가 인식함
        this.id = new FavoriteMenuId(user.getId(), menu.getId());
        this.createdAt = LocalDateTime.now();
    }
}
