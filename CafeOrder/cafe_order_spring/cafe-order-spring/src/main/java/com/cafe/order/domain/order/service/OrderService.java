package com.cafe.order.domain.order.service;

import com.cafe.order.domain.menu.dto.*;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.menustatus.entity.MenuStatus;
import com.cafe.order.domain.menustatus.entity.MenuStatusId;
import com.cafe.order.domain.menustatus.entity.SalesStatus;
import com.cafe.order.domain.menustatus.repo.JpaSellerStockRepository;
import com.cafe.order.domain.order.dto.*;
import com.cafe.order.domain.order.repo.InMemoryOrderRepository;
import com.cafe.order.domain.order.repo.JpaOrderItemRepository;
import com.cafe.order.domain.order.repo.JpaOrderRepository;
import com.cafe.order.domain.order.repo.SqlOrderRepository;
import com.cafe.order.domain.order.util.OptionPriceCalculator;
import com.cafe.order.domain.store.dto.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import com.cafe.order.domain.storemenu.repo.JpaStoreMenuRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final JpaOrderRepository orderRepository;
//    private final SqlOrderRepository orderRepository;
//    private final InMemoryOrderRepository orderRepository;

    private final JpaOrderItemRepository orderItemRepository;
    private final JpaStoreMenuRepository storeMenuRepository;
    private final JpaSellerStockRepository sellerStockRepository;
    private final JpaMenuRepository menuRepository;
    private final StoreService storeService;

    public OrderService(JpaOrderRepository orderRepository, JpaOrderItemRepository orderItemRepository, StoreService storeService, JpaStoreMenuRepository storeMenuRepository, JpaSellerStockRepository sellerStockRepository, JpaMenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.storeService = storeService;
        this.storeMenuRepository = storeMenuRepository;
        this.sellerStockRepository = sellerStockRepository;
        this.menuRepository = menuRepository;
    }


    /**
     * 관리자용
     */
    // READ : 전체 지점별 매출 조회
    public List<SalesDto> getSalesByStore() {
        // 1. COMPLETE된 주문만 가져오기
        List<Order> orders = orderRepository.findByStatus(OrderStatus.COMPLETED);

        // 2. storeId별 그룹핑 + 집계
        Map<Integer, SalesData> salesMap = new HashMap<>();

        for (Order order : orders) {
            int storeId = order.getStoreId();
            int price = order.getTotalPrice();

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

            // Store 조회
            Store store = storeService.findById(storeId);

            // DTO 생성
            result.add(new SalesDto(
                    store.getName(), // 지점명
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
    public UUID createOrder(CreateOrderRequest req, Integer storeId) {
        int size = req.getMenuId().size();
        // 전체 금액 누적용
        int totalPrice = 0;

        // OrderItem 모음
        List<OrderItem> items = new ArrayList<>();

        // 1. 입력 값 기본 검증
        // 1-1. customerId 검증
        if (req.getCustomerId() == null || req.getCustomerId().isBlank()) {
            throw new IllegalArgumentException("customerId가 비어있습니다.");
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
                    null,
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

        // 9. Order 엔티티 생성 -> 저장
        Order order = new Order(req.getCustomerId(), storeId, totalPrice, OrderStatus.ORDER_PLACED, waiting_number);

        orderRepository.save(order);

        // 10. OrderItem 전체 저장
        for (OrderItem item : items) {
            item.setOrderId(order.getOrderId());
        }

        orderItemRepository.saveAll(items);

        // 11. 최종 반환 값 - 성공 시 생성된 orderId만 반환
        return order.getOrderId();
    }

}

// 집게용 임시 클래스
class SalesData {
    int orderCount = 0;
    int totalSales = 0;
}
