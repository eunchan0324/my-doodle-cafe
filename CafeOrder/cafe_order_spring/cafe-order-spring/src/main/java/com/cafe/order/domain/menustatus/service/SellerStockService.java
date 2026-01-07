package com.cafe.order.domain.menustatus.service;

import com.cafe.order.domain.menustatus.entity.MenuStatus;
import com.cafe.order.domain.menustatus.entity.MenuStatusId;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import com.cafe.order.domain.menustatus.repo.JpaSellerStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SellerStockService {

    private final JpaSellerStockRepository sellerStockRepository;
//    private final SqlSellerStockRepository sellerStockRepository;
//    private final InMemorySellerStockRepository sellerStockRepository;


    public SellerStockService(JpaSellerStockRepository sellerStockRepository) {
        this.sellerStockRepository = sellerStockRepository;
    }


    /**
     * READ : storeId로 List<MenuStatus> 목록 반환
     */
    public List<MenuStatus> findByStoreId(Integer storeId) {
        return sellerStockRepository.findByIdStoreId(storeId);
    }

    /**
     * UPDATE : 재고/판매 상태 일괄 수정
     * - Dirty Checking을 사용해 엔티티 변경을 자동으로 반영
     * - 예외 발생 시 롤백
     */
    @Transactional
    public void updateMenuStatus(Integer storeId, UUID menuId, int stock, SalesStatus status) {
        // 1. PK 생성
        MenuStatusId id = new MenuStatusId(storeId, menuId);

        // 2. 엔티티(단건) 조회
        MenuStatus ms = sellerStockRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("MenuStatus가 존재하지 않습니다. id를 확인해주세요."));

        // 3. 재고 변경 로직
        if (stock > ms.getStock()) {
            ms.increaseStock(stock - ms.getStock());
        } else if (stock < ms.getStock()) {
            ms.decreaseStock(ms.getStock() - stock);
        }

        // 4. 상태 변경 로직
        // 판매 중지
        if (status == SalesStatus.STOP) {
            ms.stopSelling();
        }
        // 현재 STOP 상태이면서, status가 ON_SALE일 때
        else if (status == SalesStatus.ON_SALE) {
            if (ms.getStatus() == SalesStatus.STOP) {
                ms.resumeSelling();
            }
        }

        // 5. 저장 (save)
        sellerStockRepository.save(ms); // @Transactional으로 필수 X지만, 3Repo 구조를 위해 남겨둠
    }


}
