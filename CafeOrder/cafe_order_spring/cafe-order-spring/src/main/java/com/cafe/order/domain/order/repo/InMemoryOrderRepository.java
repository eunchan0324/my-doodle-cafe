package com.cafe.order.domain.order.repo;

import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.entity.OrderItem;
import com.cafe.order.domain.order.dto.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

//@Repository
public class InMemoryOrderRepository {

    private final List<Order> orders;
    private final List<OrderItem> orderItems;

    // 생성자로 초기 데이터 로드
    public InMemoryOrderRepository() {
        this.orders = new ArrayList<>();
        this.orderItems = new ArrayList<>();

        // UUID 미리 생성 (Order와 OrderItem 연결용)
        UUID order1Id = UUID.randomUUID();
        UUID order2Id = UUID.randomUUID();
        UUID order3Id = UUID.randomUUID();
        UUID order4Id = UUID.randomUUID();
        UUID order5Id = UUID.randomUUID();
        UUID order6Id = UUID.randomUUID();
        UUID order7Id = UUID.randomUUID();
        UUID order8Id = UUID.randomUUID();
        UUID order9Id = UUID.randomUUID();
        UUID order10Id = UUID.randomUUID();
        UUID order11Id = UUID.randomUUID();
        UUID order12Id = UUID.randomUUID();
        UUID order13Id = UUID.randomUUID();
        UUID order14Id = UUID.randomUUID();
        UUID order15Id = UUID.randomUUID();

        // Menu UUID (더미)
        UUID americanoId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID latteId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID cakeId = UUID.fromString("33333333-3333-3333-3333-333333333333");

        // ===== 강남점 =====
        orders.add(new Order(order1Id, "customer1", 1,
                LocalDateTime.now().minusDays(2), 5000, OrderStatus.COMPLETED, 1));
        orderItems.add(new OrderItem(
                order1Id, americanoId, "아메리카노", 4500,
                Temperature.ICE, CupType.DISPOSABLE, ShotOption.EXTRA, 1, 5000
        ));

        orders.add(new Order(order2Id, "customer2", 1,
                LocalDateTime.now().minusDays(2), 11000, OrderStatus.COMPLETED, 2));
        orderItems.add(new OrderItem(
                order2Id, latteId, "카페라떼", 5000,
                Temperature.HOT, CupType.STORE, ShotOption.NONE, 1, 5000
        ));
        orderItems.add(new OrderItem(
                order2Id, americanoId, "아메리카노", 4500,
                Temperature.ICE, CupType.DISPOSABLE, ShotOption.NONE, 1, 6000
        ));

        orders.add(new Order(order3Id, "customer1", 1,
                LocalDateTime.now().minusDays(1), 6500, OrderStatus.COMPLETED, 3));
        orderItems.add(new OrderItem(
                order3Id, latteId, "카페라떼", 5000,
                Temperature.HOT, CupType.PERSONAL, ShotOption.EXTRA, 1, 6500
        ));

        orders.add(new Order(order4Id, "customer2", 1,
                LocalDateTime.now(), 4500, OrderStatus.PREPARING, 4));
        orderItems.add(new OrderItem(
                order4Id, americanoId, "아메리카노", 4500,
                Temperature.HOT, CupType.STORE, ShotOption.NONE, 1, 4500
        ));

        // ===== 홍대점 =====
        orders.add(new Order(order5Id, "customer1", 2,
                LocalDateTime.now().minusDays(3), 10000, OrderStatus.COMPLETED, 1));
        orderItems.add(new OrderItem(
                order5Id, latteId, "카페라떼", 5000,
                Temperature.ICE, CupType.DISPOSABLE, ShotOption.NONE, 2, 10000
        ));


    }


    /** 관리자용 */
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


    /**
     * 판매자용
     */
    // READ : storeId로 List<Order> 조회
    public List<Order> findByStoreId(Integer storeId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStoreId().equals(storeId)) {
                // OrderItem 찾아서 설정
                List<OrderItem> items = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    if (item.getOrderId().equals(order.getOrderId())) {
                        items.add(item);
                    }
                }
                order.setItems(items);
                result.add(order);
            }
        }

        return result;
    }

    // READ : OrderId로 Order(+OrderItem) 조회 (람다식)
    public Optional<Order> findById(UUID orderId) {
        // 1. Order 찾기
        Order order = orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);

        // 2. OrderItem 찾기
        if (order != null) {
            List<OrderItem> items = orderItems.stream()
                    .filter(item -> item.getOrderId().equals(orderId))
                    .collect(Collectors.toList());
            order.setItems(items);
        }

        return Optional.ofNullable(order);
    }

    // READ : OrderId로 Order(+OrderItem) 조회 (원시 자바 반복문)
    @Deprecated
    public Optional<Order> findByIdBasedRoop(UUID orderId) {
        Order order = null;

        // 1. Order 찾기
        for (Order o : orders) {
            if (o.getOrderId().equals(orderId)) {
                order = o;
                break;
            }
        }

        // 2. 없으면 null 반환
        if (order == null) {
            return null;
        }

        // 3. OrderItem 찾기 (orderId로 필터링)
        List<OrderItem> items = new ArrayList<>();
        for (OrderItem item : orderItems) {
            if (item.getOrderId().equals(orderId)) {
                items.add(item);
            }
        }

        // 4. Order에 items 설정
        order.setItems(items);

        return Optional.of(order);
    }

    // READ : storeId + OrderStatus로 List<Order> 조회 (람다식)
    public List<Order> findByStoreIdAndStatus(Integer storeId, OrderStatus status) {
        return orders.stream()
                .filter(order -> order.getStoreId().equals(storeId) && order.getStatus().equals(status))
                .map(order -> {
                    List<OrderItem> items = orderItems.stream()
                            .filter(orderItem -> orderItem.getOrderId().equals(order.getOrderId()))
                            .collect(Collectors.toList());
                    order.setItems(items);
                    return order;
                })
                .collect(Collectors.toList());
    }

    // READ : storeId + OrderStatus로 List<Order> 조회 (자바 반복문)
    @Deprecated
    public List<Order> findByStoreIdAndStatusBasedRoop(Integer storeId, OrderStatus status) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStoreId().equals(storeId) && order.getStatus().equals(status)) {
                // OrderItem 찾아서 설정
                List<OrderItem> items = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    if (item.getOrderId().equals(order.getOrderId())) {
                        items.add(item);
                    }
                }
                order.setItems(items);
                result.add(order);
            }
        }

        return result;
    }

    // UPDATE : 주문 상태 변경
    public Order update(Order order) {
        for (Order o : orders) {
            if (o.getOrderId().equals(order.getOrderId())) {
                o.setStatus(order.getStatus());
                break;

            }
        }
        return order;
    }

    /**
     * 구매자용
     */
    /**
     * READ : 주문 목록 확인
     */
    public List<Order> findByStoreIdAndCustomerId(Integer storeId, String customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStoreId().equals(storeId) && order.getCustomerId().equals(customerId)) {

                // OrderItem 설정
                List<OrderItem> items = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    if (item.getOrderId().equals(order.getOrderId())) {
                        items.add(item);
                    }
                }

                // 복사본 생성
                Order copy = new Order(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getStoreId(),
                        order.getOrderTime(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getWaitingNumber()
                );
                order.setItems(items);
                result.add(order);
            }
        }

        return result;
    }
}
