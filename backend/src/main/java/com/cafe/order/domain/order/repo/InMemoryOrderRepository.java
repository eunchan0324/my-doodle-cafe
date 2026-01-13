package com.cafe.order.domain.order.repo;

import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import com.cafe.order.domain.order.dto.OrderStatus;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.entity.OrderItem;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.entity.UserRole;

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

        // UUID 미리 생성
        UUID order1Id = UUID.randomUUID();
        UUID order2Id = UUID.randomUUID();
        UUID order3Id = UUID.randomUUID();
        UUID order4Id = UUID.randomUUID();
        UUID order5Id = UUID.randomUUID();

        // Menu UUID (더미)
        UUID americanoId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID latteId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        User user1 = new User("customer1", "1234", "김철수", UserRole.CUSTOMER);
        user1.setId(1); // PK도 가짜로 넣어줌

        User user2 = new User("customer2", "1234", "이영희", UserRole.CUSTOMER);
        user2.setId(2);

        Store store1 = new Store("강남점");
        store1.setId(1); // 가짜 ID 부여

        Store store2 = new Store("홍대점");
        store2.setId(2);

// ===== 강남점 (storeId: 1 -> store1 객체) =====
        // Order 생성
        Order order1 = new Order(user1, store1, 5000, OrderStatus.COMPLETED, 1);
        order1.setOrderId(order1Id);
        orders.add(order1);

        // OrderItem 생성
        OrderItem item1 = new OrderItem(
            americanoId, "아메리카노", 4500,
            Temperature.ICE, CupType.DISPOSABLE, ShotOption.EXTRA, 1, 5000
        );

        // 연관 관계 맺기
        order1.addOrderItem(item1);
        orderItems.add(item1);

        orders.add(new Order(user1, store1, 5000, OrderStatus.COMPLETED, 1)); // ✅ store1 전달
        orders.get(0).setOrderId(order1Id);

        // Order 2
        Order order2 = new Order(user2, store1, 11000, OrderStatus.COMPLETED, 2);
        order2.setOrderId(order2Id);
        orders.add(order2);

        OrderItem item2_1 = new OrderItem(
            latteId, "카페라떼", 5000,
            Temperature.HOT, CupType.STORE, ShotOption.NONE, 1, 5000
        );
        OrderItem item2_2 = new OrderItem(
            americanoId, "아메리카노", 4500,
            Temperature.ICE, CupType.DISPOSABLE, ShotOption.NONE, 1, 6000
        );
        order2.addOrderItem(item2_1);
        order2.addOrderItem(item2_2);
        orderItems.add(item2_1);
        orderItems.add(item2_2);
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
            if (order.getStore().getId().equals(storeId)) {
                // OrderItem 찾아서 설정
                List<OrderItem> items = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    if (item.getOrder().getOrderId().equals(order.getOrderId())) {
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
                    .filter(item -> item.getOrder().getOrderId().equals(orderId))
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
            if (item.getOrder().getOrderId().equals(orderId)) {
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
                .filter(order -> order.getStore().getId().equals(storeId) && order.getStatus().equals(status))
                .map(order -> {
                    List<OrderItem> items = orderItems.stream()
                            .filter(orderItem -> orderItem.getOrder().getOrderId().equals(order.getOrderId()))
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
            if (order.getStore().getId().equals(storeId) && order.getStatus().equals(status)) {
                // OrderItem 찾아서 설정
                List<OrderItem> items = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    if (item.getOrder().getOrderId().equals(order.getOrderId())) {
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

    // ========================
    // 구매자용
    // ========================

    /**
     * READ : 주문 목록 확인
     */
    public List<Order> findByStoreIdAndCustomerId(Integer storeId, String customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStore().getId().equals(storeId) &&
                order.getUser().getLoginId().equals(customerId)) {

                // OrderItem 설정
                List<OrderItem> items = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    if (item.getOrder().getOrderId().equals(order.getOrderId())) {
                        items.add(item);
                    }
                }

                // 복사본 생성
                Order copy = new Order(
                        order.getOrderId(),
                        order.getUser(),
                        order.getStore(),
                        order.getOrderTime(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getWaitingNumber()
                );
                copy.setOrderId(order.getOrderId());
                copy.setItems(items);
                result.add(copy);
            }
        }
        return result;
    }
}
