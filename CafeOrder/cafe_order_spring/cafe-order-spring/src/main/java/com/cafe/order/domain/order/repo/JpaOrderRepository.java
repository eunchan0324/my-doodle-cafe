package com.cafe.order.domain.order.repo;

import com.cafe.order.domain.order.dto.Order;
import com.cafe.order.domain.order.dto.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByStoreId(Integer storeId);

}
