package com.cafe.order.domain.order.service;

import com.cafe.order.domain.cart.dto.CustomerCartItem;
import com.cafe.order.domain.cart.service.CartService;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
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
import com.cafe.order.domain.storemenu.service.StoreMenuService;
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

    private final JpaStoreMenuRepository storeMenuRepository;
    private final StoreService storeService;
    private final CartService cartService;
    private final JpaUserRepository userRepository;
    private final StoreMenuService storeMenuService;

    // ======= REST API =======

    /**
     * 주문 생성 메서드 (Stateless)
     * - 클라이언트가 보낸 JSON 데이터를 기반으로 주문을 생성
     * - 세션 장바구니를 쓰지 않고, 요청 데이텨(request)를 전적으로 신뢰하여 처리
     *
     * @param userId  주문하는 사용자 ID (JWT에서 추출)
     * @param request 주문 상세 정보가 담긴 DTO (지점 ID, 아이템 리스트 등)
     * @return 생성된 주문의 UUID
     */
    @Transactional
    public Order createOrder(Integer userId, OrderCreateRequest request) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 지점 조회
        Store store = storeService.findById(request.getStoreId());
        if (store == null) {
            throw new IllegalArgumentException("유효하지 않은 storeId입니다.");
        }

        // 3. 아이템 처리 및 총 가격 계산을 위한 변수 준비
        List<OrderItem> orderItems = new ArrayList<>();
        int totalPrice = 0;

        // 4. 요청받은 아이템 리스트
        for (OrderCreateRequest.ItemRequest itemReq : request.getItems()) {

            // 4-1. 메뉴 판매 가능 여부 검증
            UUID menuId = itemReq.getMenuId();

            StoreMenu storeMenu = storeMenuRepository.findByStore_IdAndMenu_Id(store.getId(), menuId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 지점에서 판매하지 않는 메뉴입니다."));

            if (!storeMenu.isSellable()) {
                throw new IllegalArgumentException("주문할 수 없는 메뉴입니다: " + storeMenu.getMenu().getName());
            }

            // 4-2. 재고 차감
            storeMenu.decreaseStock(itemReq.getQuantity());

            // 4-3. 가격 계산
            Menu menu = storeMenu.getMenu();

            // 옵션 가격
            int optionPrice = 0;
            optionPrice += itemReq.getCupType().getPriceDelta(); // 컵 사이즈
            optionPrice += itemReq.getShotOption().getPriceDelta(); // 샷 추가

            // 최종 아이템 1개 가격 (기본가 + 옵션가)
            int unitPrice = menu.getPrice() + optionPrice;

            // 해당 라인 총 가격 (단가 * 수량)
            int lineTotalPrice = unitPrice * itemReq.getQuantity();

            // 전체 주문 금액에 누적
            totalPrice += lineTotalPrice;

            // 4-4. OrderItem 엔티티 만들기
            OrderItem orderItem = new OrderItem(
                    menu.getId(),
                    menu.getName(),
                    menu.getPrice(),
                    itemReq.getTemperature(),
                    itemReq.getCupType(),
                    itemReq.getShotOption(),
                    itemReq.getQuantity(),
                    lineTotalPrice
            );

            // 4-5 리스트에 추가
            orderItems.add(orderItem);
        }

        // 5. 대기번호 생성
        LocalDate today = LocalDate.now();
        Integer waitingNumber = orderRepository.findMaxWaitingNumberForStoreToday(request.getStoreId(), today);

        waitingNumber = (waitingNumber == null) ? 1 : waitingNumber + 1;

        // 6. Order 엔티티 생성
        Order order = new Order(user, store, totalPrice, OrderStatus.ORDER_PLACED, waitingNumber);

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        orderRepository.save(order);

        return order;
    }

    /**
     * 구매자의 모든 주문 내역 조회 (전체 지점 합산)
     * - 특정 지점에 상관없이 사용자가 주문한 모든 내역을 최신순으로 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 주문 요약 정보(DTO) 리스트
     */
    public List<CustomerOrderSummary> findOrderByUserId(Integer userId) {
        // 1. DB에서 해당 사용자의 모든 주문을 최신순으로 조회
        List<Order> orders = orderRepository.findByUserIdOrderByOrderTimeDesc(userId);

        // 2. 조회된 Order 엔티티 리스트를 CustomerOrderSummary DTO로 변환
        // -stream()을 사용하여 각 Order 객체를 CustomerOrderSummary 생성자에 넘겨 변환
        return orders.stream()
                .map(CustomerOrderSummary::new)
                .toList();
    }


    // ======= 관리자용 =======

    /**
     * READ : 전체 지점별 매출 조회
     */
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


    // ======= 판매자용 =======

    /**
     * 주문 관리 메뉴
     */
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
    public void updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        order.setStatus(newStatus);
        orderRepository.save(order); // JPA
//      orderRepository.update(order); // SQL, InMemory
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


    // ======= 구매자용 =======

    /**
     * READ : 주문 내역 확인
     */
    public List<CustomerOrderSummary> findOrderSummaries(Integer storeId, Integer userId) {

        List<Order> orders = orderRepository.findByStoreIdAndUserId(storeId, userId);

        return orders.stream()
                .map(CustomerOrderSummary::new) // Order -> DTO 변환
                .collect(Collectors.toList());
    }

    /**
     * CREATE : 장바구니(세션) 데이터 기반 최종 주문을 생성하고 저장
     */
    @Transactional
    public UUID createOrderFromCart(Integer userId, Integer storeId, HttpSession session) {
        List<OrderItem> items = new ArrayList<>();
        Integer totalPrice = 0;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // Store 조회 (Order 생성에 필요)
        Store store = storeService.findById(storeId);
        if (store == null) {
            throw new IllegalArgumentException("유효하지 않은 storeId입니다.");
        }

        // 1. 장바구니 데이터 가져오기 (CartService 활용)
        List<CustomerCartItem> cartItems = cartService.getCartItems(userId, session);

        // 2. 주문 유효성 검증 및 OrderItem 생성
        for (CustomerCartItem item : cartItems) {
            if (item == null) continue;

            UUID menuId = item.getMenuId();

            // StoreMenu 조회
            StoreMenu sm = storeMenuRepository
                    .findByStore_IdAndMenu_Id(storeId, menuId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 지점에서 판매하지 않는 메뉴입니다."));

            // 통합된 판매 가능 여부 확인
            if (!sm.isSellable()) {
                throw new IllegalArgumentException("구매할 수 없는 메뉴입니다(품절/판매중지): " + sm.getMenu().getName());
            }

            // 재고 차감
            sm.decreaseStock(item.getQuantity());

            Menu menu = sm.getMenu();

            // 3. 총 금액 누적 및 OrderItem 리스트 생성
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

        // 4. waiting_number 생성
        LocalDate today = LocalDate.now();
        Integer waiting_number = orderRepository.findMaxWaitingNumberForStoreToday(storeId, today);
        waiting_number = (waiting_number == null) ? 1 : waiting_number + 1;

        // 5. Order 엔티티 생성 및 저장
        Order order = new Order(user, store, totalPrice, OrderStatus.ORDER_PLACED, waiting_number);

        for (OrderItem item : items) {
            order.addOrderItem(item); // OrderItem의 order 엔티티 자동 저장
        }

        orderRepository.save(order);

        // 6. 장바구니 데이터 비우기 (세션에서 제거)
        String cartSessionKey = "customer_cart_" + userId;
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
