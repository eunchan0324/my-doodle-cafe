package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.storemenu.entity.StoreMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaStoreMenuRepository extends JpaRepository<StoreMenu, Integer> {

    List<StoreMenu> findByStore_Id(Integer storeId);

    Optional<StoreMenu> findByStore_IdAndMenu_Id(Integer storeId, UUID menuId);
}
