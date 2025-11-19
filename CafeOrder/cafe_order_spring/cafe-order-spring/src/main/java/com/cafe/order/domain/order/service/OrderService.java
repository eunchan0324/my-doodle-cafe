package com.cafe.order.domain.order.service;

import com.cafe.order.domain.order.dto.Order;
import com.cafe.order.domain.order.dto.OrderStatus;
import com.cafe.order.domain.order.dto.SalesDto;
import com.cafe.order.domain.order.repo.InMemoryOrderRepository;
import com.cafe.order.domain.order.repo.JpaOrderRepository;
import com.cafe.order.domain.order.repo.SqlOrderRepository;
import com.cafe.order.domain.store.dto.Store;
import com.cafe.order.domain.store.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private final JpaOrderRepository orderRepository;
//    private final SqlOrderRepository orderRepository;
//    private final InMemoryOrderRepository orderRepository;

    private final StoreService storeService;

    public OrderService(JpaOrderRepository orderRepository, StoreService storeService) {
        this.orderRepository = orderRepository;
        this.storeService = storeService;
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
        return orderRepository.save(order);
    }


}

// 집게용 임시 클래스
class SalesData {
    int orderCount = 0;
    int totalSales = 0;
}
