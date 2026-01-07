package com.cafe.order.domain.order.service;

import com.cafe.order.domain.cart.dto.CustomerCartItem;
import com.cafe.order.domain.cart.service.CartService;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.menustatus.entity.MenuStatus;
import com.cafe.order.domain.menustatus.entity.MenuStatusId;
import com.cafe.order.domain.menustatus.repo.JpaSellerStockRepository;
import com.cafe.order.domain.order.dto.*;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.entity.OrderItem;
import com.cafe.order.domain.order.repo.JpaOrderItemRepository;
import com.cafe.order.domain.order.repo.JpaOrderRepository;
import com.cafe.order.domain.order.util.OptionPriceCalculator;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
import com.cafe.order.domain.storemenu.repo.JpaStoreMenuRepository;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.repo.JpaUserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final JpaOrderRepository orderRepository;
//    private final SqlOrderRepository orderRepository;
//    private final InMemoryOrderRepository orderRepository;

    private final JpaOrderItemRepository orderItemRepository;
    private final JpaStoreMenuRepository storeMenuRepository;
    private final JpaSellerStockRepository sellerStockRepository;
    private final JpaMenuRepository menuRepository;
    private final StoreService storeService;
    private final CartService cartService;
    private final JpaUserRepository userRepository;


    /**
     * 관리자용
     */
    // READ : 전체 지점별 매출 조회
    public List<SalesDto> getSalesByStore() {
        // 1. COMPLETE된 주문만 가져오기
        List<Order> orders = orderRepository.findByStatus(OrderStatus.COMPLETED);

        // 2. storeId별 그룹핑 + 집계
        Map<Integer, SalesData> salesMap = new HashMap<>();

        // 나중에 DB 조회를 또 하지 않기 위해 store 이름만 따로 캐싱해둠 (JPA 장점 활용!)
        Map<Integer, String> storeNameMap = new HashMap<>();

        for (Order order : orders) {
            Store store = order.getStore();
            int storeId = store.getId();
            int price = order.getTotalPrice();

            // 가게 이름 미리 저장 (store 객체가 이미 있으니까 공짜!)
            storeNameMap.putIfAbsent(storeId, store.getName());

            // 그룹핑 : storeId를 key로
            if (!salesMap.containsKey(storeId)) {
                salesMap.put(storeId, new SalesData());
            }

            // 집계 : count 증가, 금액 누적
            SalesData data = salesMap.get(storeId);
            data.orderCount++; // 주문수 +1
            data.totalSales += price; // 매출 누적
        }

        // 3. Store 이름 조회 + DTO 반환
        List<SalesDto> result = new ArrayList<>();
        for (Map.Entry<Integer, SalesData> entry : salesMap.entrySet()) {
            int storeId = entry.getKey();
            SalesData data = entry.getValue();

            // 저장해둔 Map에서 이름만 꺼냄
            String storeName = storeNameMap.get(storeId);

            // DTO 생성
            result.add(new SalesDto(
                    storeName, // 지점명
                    data.orderCount, //주문수
                    data.totalSales // 총매출
            ));
        }
        return result;
    }


    /**
     * 판매자용
     */

    /**
     * 주문 관리 메뉴
     */
    // READ : 특정 지점 주문 목록 조회
    public List<Order> findByStoreId(Integer storeId) {
        return orderRepository.findByStoreId(storeId);
    }

    // READ : 특정 지점 주문 목록 조회 (COMPLETED 제외)
    public List<Order> findActiveOrderByStoreId(Integer storeId) {
        List<Order> allOrders = orderRepository.findByStoreId(storeId);

        // COMPLETED 제외
        List<Order> activeOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (!(order.getStatus() == OrderStatus.COMPLETED)) {
                activeOrders.add(order);
            }
        }

        // Lazy Loading 강제 실행
        for (Order order : activeOrders) {
            if (order.getItems() != null) {
                order.getItems().size();
            }
        }

        return activeOrders;
    }

    // READ : 주문 상세 조회 (OrderItem 포함)
    public Order findById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // Lazy Loading 강제 실행 (items 사용)
        if (order.getItems() != null) {
            order.getItems().size(); // items를 미리 로드
        }

        return order;
    }

    // UPDATE : 주문 상태 변경
    public Order updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        order.setStatus(newStatus);
        return orderRepository.save(order); // JPA
//        return orderRepository.update(order); // SQL, InMemory
    }

    /**
     * 매출 관리 메뉴
     */
    // READ : 특정 지점 완료된 주문 목록 조회 (COMPLETE)
    public List<Order> findCompleteOrdersByStoreId(Integer storeId) {
        List<Order> orders = orderRepository.findByStoreIdAndStatus(storeId, OrderStatus.COMPLETED);

        // Lazy Loading 강제 실행
        // View에서 order.getItems() 사용하면 → 필요
        for (Order order : orders) {
            if (order.getItems() != null) {
                order.getItems().size(); // ← DB에서 OrderItem 미리 가져오기 (원래 의미는 “개수 세기”지만, JPA에서는 Lazy 로딩을 강제로 실행시키는 트리거 역할)
            }
        }

        return orders;
    }

    // 총 매출 계산
    public int getTotalSales(Integer storeId) {
        List<Order> orders = orderRepository.findByStoreIdAndStatus(storeId, OrderStatus.COMPLETED);

        return orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();
    }

    // 총 매출 계산 (원시 자바 반복문)
    @Deprecated
    public int getTotalSalesBasedRoop(Integer storeId) {
        List<Order> orders = orderRepository.findByStoreIdAndStatus(storeId, OrderStatus.COMPLETED);

        int totalSales = 0;

        for (Order order : orders) {
            totalSales += order.getTotalPrice();
        }

        return totalSales;
    }


    /**
     * 구매자용
     */
    /**
     * CREATE : 구매자의 주문 요청을 받아 Order + OrderItem 전체를 생성하고 저장
     */
    @Transactional
    public UUID createOrder(CreateOrderRequest req, Integer storeId) {
        int size = req.getMenuId().size();
        // 전체 금액 누적용
        int totalPrice = 0;

        // OrderItem 모음
        List<OrderItem> items = new ArrayList<>();

        // 1. 입력 값 기본 검증
        // 1-1. userId 검증
        if (req.getUserId() == null) {
            throw new IllegalArgumentException("userId가 비어있습니다.");
        }

        // 1-2. 리스트 길이 검증
        if (size == 0) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
        if (req.getQuantity().size() != size ||
                req.getTemperature().size() != size ||
                req.getCupType().size() != size ||
                req.getOptions().size() != size) {
            throw new IllegalArgumentException("리스트 길이가 서로 다릅니다.");
        }

        // 2. Store 검증
        Store store = storeService.findById(storeId);
        if (store == null) {
            throw new IllegalArgumentException("유효하지 않은 storeId입니다.");
        }

        // 2-1. User 조회
        User user = userRepository.findById(req.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + req.getUserId()));

        // 3. 각 메뉴 검증 (판매 여부 + 품절 여부)
        List<UUID> menuIds = req.getMenuId();

        for (int i = 0; i < menuIds.size(); i++) {
            UUID menuId = menuIds.get(i);

            // 3-1. 지점에서 판매하는 메뉴인지 확인
            StoreMenu sm = storeMenuRepository
                    .findByStoreIdAndMenuId(storeId, menuId)
                    .orElseThrow(() -> new IllegalArgumentException("판매하지 않는 메뉴입니다: " + menuId));

            // 3-2. 해당 메뉴의 MenuStatus 검증
            MenuStatusId msId = new MenuStatusId(storeId, menuId);

            MenuStatus ms = sellerStockRepository.findById(msId)
                    .orElseThrow(() -> new IllegalArgumentException("MenuStatus가 존재하지 않습니다: " + menuId));

            if (!ms.isSellable()) {
                throw new IllegalArgumentException("판매 불가능한 메뉴입니다(재고=0, 품절, 중지 등): " + menuId);
            }


            // 4. 메뉴 정보 가져오기 (가격, 메뉴명 등)
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴:" + menuId));

            String menuName = menu.getName();
            Integer menuPrice = menu.getPrice();

            // 5. 옵션 포함 단가 계산
            int unitPrice = OptionPriceCalculator.calculate(
                    menu.getCategory(),
                    menuPrice,
                    req.getTemperature().get(i),
                    req.getCupType().get(i),
                    req.getOptions().get(i)
            );

            // 옵션이 포함된 총 가격
            int finalPrice = unitPrice * req.getQuantity().get(i);

            // 6. 주문 전체 금액 (totalPrice) 계산
            totalPrice += finalPrice;

            // 7. OrderItem 리스트 생성
            OrderItem orderItem = new OrderItem(
                    menuId,
                    menuName,
                    menuPrice,
                    req.getTemperature().get(i),
                    req.getCupType().get(i),
                    req.getOptions().get(i),
                    req.getQuantity().get(i),
                    finalPrice
            );

            items.add(orderItem);
        }

        // 8. waiting_number 생성
        LocalDate today = LocalDate.now();
        Integer waiting_number = orderRepository.findMaxWaitingNumberForStoreToday(storeId, today);
        if (waiting_number == null) {
            waiting_number = 1;
        } else {
            waiting_number += 1;
        }

        // 9. Order. OrderItem 생성 및 저장
        Order order = new Order(user, store, totalPrice, OrderStatus.ORDER_PLACED, waiting_number);

        // 연관 관계 설정 (메서드에서 item 쪽에도 order를 더해줌)
        for (OrderItem item : items) {
            order.addOrderItem(item);
        }

        // 저장
        orderRepository.save(order);

        // 11. 최종 반환 값 - 성공 시 생성된 orderId만 반환
        return order.getOrderId();
    }

    /**
     * READ : 주문 내역 확인
     */
    public List<CustomerOrderSummary> findOrderSummaries(Integer storeId, String customerId) {

        List<Order> orders = orderRepository.findByStoreIdAndUserLoginId(storeId, customerId);

        return orders.stream()
                .map(CustomerOrderSummary::new) // Order -> DTO 변환
                .collect(Collectors.toList());
    }

    /**
     * CREATE : 장바구니(세션) 데이터 기반 최종 주문을 생성하고 저장
     */
    @Transactional
    public UUID createOrderFromCart(String customerId, Integer storeId, HttpSession session) {
        List<OrderItem> items = new ArrayList<>();
        Integer totalPrice = 0;

        // todo : customerId 자체 컨트롤러 리팩토링 이후 수정 필요
        Integer userId = Integer.parseInt(customerId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 1. 장바구니 데이터 가져오기 (CartService 활용)
        List<CustomerCartItem> cartItems = cartService.getCartItems(customerId, session);

        // 2. 주문 유효성 검증 및 메뉴 상태 재확인 (재고, 품절 등)
        for (CustomerCartItem item : cartItems) {
            if (item == null) {
                throw new IllegalArgumentException("유효하지 않은 장바구니입니다.");
            }

            UUID menuId = item.getMenuId();

            StoreMenu sm = storeMenuRepository
                    .findByStoreIdAndMenuId(storeId, menuId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 지점에서 판매하지 않는 메뉴입니다."));

            MenuStatusId msId = new MenuStatusId(storeId, menuId);

            MenuStatus ms = sellerStockRepository.findById(msId)
                    .orElseThrow(() -> new IllegalArgumentException("MenuStatus가 존재하지 않습니다."));

            if (!ms.isSellable()) {
                throw new IllegalArgumentException("판매가 불가능한 메뉴입니다.");
            }

            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

            // 3. OrderItem 리스트 생성 및 총 금액 계산
            totalPrice += item.getFinalPrice();

            OrderItem orderItem = new OrderItem(
                    menuId,
                    item.getName(),
                    menu.getPrice(),
                    item.getTemperature(),
                    item.getCupType(),
                    item.getShotOption(),
                    item.getQuantity(),
                    item.getFinalPrice()
            );

            items.add(orderItem);
        }

        // 4. Order 엔티티 생성 및 저장 (waiting_number 포함)
        LocalDate today = LocalDate.now();
        Integer waiting_number = orderRepository.findMaxWaitingNumberForStoreToday(storeId, today);
        if (waiting_number == null) {
            waiting_number = 1;
        } else {
            waiting_number += 1;
        }

        Store store = storeService.findById(storeId);
        if (store == null) {
            throw new IllegalArgumentException("유효하지 않은 storeId입니다.");
        }

        Order order = new Order(user, store, totalPrice, OrderStatus.ORDER_PLACED, waiting_number);

        for (OrderItem item : items) {
            order.addOrderItem(item); // OrderItem의 order 엔티티 자동 저장
        }

        orderRepository.save(order);

        // 6. 장바구니 데이터 비우기 (세션에서 제거)
        String cartSessionKey = "customer_cart_" + customerId;
        session.removeAttribute(cartSessionKey);

        // 최종 반환
        return order.getOrderId();
    }
}

// 집게용 임시 클래스
class SalesData {
    int orderCount = 0;
    int totalSales = 0;
}
