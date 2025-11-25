package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.storemenu.dto.StoreMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaStoreMenuRepository extends JpaRepository<StoreMenu, Integer> {


    List<StoreMenu> findByStoreId(Integer storeId);
}
