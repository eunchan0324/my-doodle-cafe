package com.cafe.order.domain.order.repo;

import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.dto.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByStoreId(Integer storeId);

    List<Order> findByStoreIdAndStatus(Integer storeId, OrderStatus orderStatus);

    // todo : query DSL로 리팩토링 가능한지 확인
    @Query(
            value = "SELECT MAX(waiting_number) " +
                    "FROM orders " +
                    "WHERE store_id = :storeId " +
                    "AND CAST(order_time AS DATE) = :today",
            nativeQuery = true)
    Integer findMaxWaitingNumberForStoreToday(
            @Param("storeId") Integer storeId,
            @Param("today") LocalDate today
    );

    List<Order> findByStoreIdAndUserLoginId(Integer storeId, String loginId);
}
