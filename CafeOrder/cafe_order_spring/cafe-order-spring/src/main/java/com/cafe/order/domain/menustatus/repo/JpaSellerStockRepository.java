package com.cafe.order.domain.menustatus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSellerStockRepository extends JpaRepository<MenuStatus, MenuStatusId> {

    List<MenuStatus> findByIdStoreId(Integer storeId);
}
