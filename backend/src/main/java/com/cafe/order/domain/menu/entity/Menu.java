package com.cafe.order.domain.menu.entity;

import com.cafe.order.common.entity.BaseEntity;
import com.cafe.order.domain.menu.dto.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    @Column(length = 500)
    private String description;

    // 생성자
    public Menu(String name, Integer price, Category category) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // 생성자 오버로딩 (설명 포함)
    public Menu(String name, Integer price, Category category, String description) {
        this(name, price, category);
        this.description = description;
    }
}
