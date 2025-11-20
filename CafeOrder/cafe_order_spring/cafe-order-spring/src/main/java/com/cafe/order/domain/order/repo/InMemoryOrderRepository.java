package com.cafe.order.domain.order.repo;

import com.cafe.order.domain.order.dto.Order;
import com.cafe.order.domain.order.dto.OrderStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//@Repository
public class InMemoryOrderRepository {

    private final List<Order> orders;

    // 생성자로 초기 데이터 로드
    public InMemoryOrderRepository() {
        this.orders = new ArrayList<>();

        // 강남점 (storeId=1) 주문
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 1,
            LocalDateTime.now().minusDays(2), 5000, OrderStatus.COMPLETED, 1
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer2", 1,
            LocalDateTime.now().minusDays(2), 11000, OrderStatus.COMPLETED, 2
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 1,
            LocalDateTime.now().minusDays(1), 6500, OrderStatus.COMPLETED, 1
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer2", 1,
            LocalDateTime.now(), 4500, OrderStatus.PREPARING, 1
        ));

        // 홍대점 (storeId=2) 주문
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 2,
            LocalDateTime.now().minusDays(3), 10000, OrderStatus.COMPLETED, 1
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer2", 2,
            LocalDateTime.now().minusDays(1), 12000, OrderStatus.COMPLETED, 1
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 2,
            LocalDateTime.now(), 5500, OrderStatus.READY, 1
        ));

        // 신촌점 (storeId=3) 주문
        orders.add(new Order(
            UUID.randomUUID(), "customer2", 3,
            LocalDateTime.now().minusDays(4), 15000, OrderStatus.COMPLETED, 1
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 3,
            LocalDateTime.now().minusDays(2), 6000, OrderStatus.COMPLETED, 2
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer2", 3,
            LocalDateTime.now().minusDays(1), 11500, OrderStatus.COMPLETED, 3
        ));

        // 잠실점 (storeId=4) 주문
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 4,
            LocalDateTime.now().minusDays(1), 9000, OrderStatus.COMPLETED, 1
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer2", 4,
            LocalDateTime.now(), 5000, OrderStatus.ORDER_PLACED, 1
        ));

        // 판교점 (storeId=5) 주문
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 5,
            LocalDateTime.now().minusDays(5), 12500, OrderStatus.COMPLETED, 1
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer2", 5,
            LocalDateTime.now().minusDays(3), 18000, OrderStatus.COMPLETED, 2
        ));
        orders.add(new Order(
            UUID.randomUUID(), "customer1", 5,
            LocalDateTime.now().minusDays(1), 7000, OrderStatus.COMPLETED, 3
        ));
    }

    // READ : status로 필터링 (람다식)
    public List<Order> findByStatus(OrderStatus status) {
        return orders.stream()
            .filter(order -> order.getStatus() == status)
            .collect(Collectors.toList());
    }

    // READ : status로 필터링 (자바 원시 코드)
    @Deprecated
    public List<Order> findByStatusBasedRoop(OrderStatus status) {
        List<Order> result = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }

        return result;
    }





}
