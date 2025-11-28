package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.storemenu.dto.StoreMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaStoreMenuRepository extends JpaRepository<StoreMenu, Integer> {


    List<StoreMenu> findByStoreId(Integer storeId);

    Optional<StoreMenu> findByStoreIdAndMenuId(Integer storeId, UUID menuId);
}
