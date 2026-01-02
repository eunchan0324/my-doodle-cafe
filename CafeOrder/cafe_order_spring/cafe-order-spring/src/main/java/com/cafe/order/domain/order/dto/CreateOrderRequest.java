package com.cafe.order.domain.order.dto;

import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderRequest {

    // 주문 단위 값
    // TODO : 로그인 기능 구현 이후 @Setter 삭제 필요
    private Integer userId;
    private OrderType orderType;

    // 여러 메뉴가 들어가므로 리스트
    private List<UUID> menuId;
    private List<Temperature> temperature;
    private List<CupType> cupType;
    private List<ShotOption> options;
    private List<Integer> quantity;

    // todo : OrderItem에 해당되는 내용들을 객체로 묶어보기
}
