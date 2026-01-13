package com.cafe.order.domain.order.repo;

import com.cafe.order.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderItemRepository extends JpaRepository <OrderItem, Integer> {
}
