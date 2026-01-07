package com.cafe.order.domain.storemenu.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalesStatus {
    READY("준비중"),
    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    STOP("판매중지");

    private final String displayName;
}
