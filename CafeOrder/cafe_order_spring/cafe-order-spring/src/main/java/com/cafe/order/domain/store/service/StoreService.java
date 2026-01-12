package com.cafe.order.domain.store.service;

import com.cafe.order.domain.store.repo.JpaStoreRepository;
import com.cafe.order.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final JpaStoreRepository storeRepository;
//    private final SqlStoreRepository storeRepository;
//    private final InMemoryStoreRepository storeRepository;

    // CREATE : 지점 생성
    public Store create(String name) {
        Store store = new Store(name);
        return storeRepository.save(store);
    }

    // READ : 전체 지점 조회
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    // READ : 지점 id로 Store 반환
    // TODO : Store 객체들을 Optional<T>로 전체 변경해주기
    public Store findById(Integer id) {
        return storeRepository.findById(id).orElse(null);
    }

    // UPDATE : 지점 수정
    public Store update(Integer id, String name) {
        Store store = storeRepository.findById(id).orElse(null);

        if (store == null) {
            throw new IllegalArgumentException("지점을 찾을 수 없습니다.");
        }

        store.setName(name);

        return storeRepository.save(store); // JPA
//        return storeRepository.update(store); // SQL, InMemory
    }

    // DELETE : 지점 삭제
    public void delete(Integer id) {
        storeRepository.deleteById(id);
    }

    // 특정 ID를 제외한 지점 목록
    public List<Store> findAvailableStores(List<Integer> excludeIds) {
        List<Store> allStores = storeRepository.findAll();

        return allStores.stream()
                .filter(store -> !excludeIds.contains(store.getId()))
                .collect(Collectors.toList());
    }
}
