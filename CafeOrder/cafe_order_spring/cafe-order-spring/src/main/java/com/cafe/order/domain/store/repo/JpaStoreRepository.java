package com.cafe.order.domain.store.repo;

import com.cafe.order.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaStoreRepository extends JpaRepository<Store, Integer> {
}
