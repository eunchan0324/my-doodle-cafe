package com.cafe.order.domain.menu.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@Getter
@Table
@Entity
public class MenuStatus {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private Integer storeId;
    private UUID menuId;

    @Enumerated(EnumType.STRING)
    private SalesStatus status;
    private Integer stock;



}
