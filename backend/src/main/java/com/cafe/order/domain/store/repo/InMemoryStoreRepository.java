package com.cafe.order.domain.store.repo;

import com.cafe.order.domain.store.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryStoreRepository {

    private final List<Store> stores;
    private Integer nextId = 1;

    // 생성자로 DB 직접 초기화
    public InMemoryStoreRepository() {
        this.stores = new ArrayList<>();

        // 초기 데이터 (ID 직접 할당)
        Store s1 = new Store("강남점");
        s1.setId(nextId++);
        stores.add(s1);

        Store s2 = new Store("홍대점");
        s2.setId(nextId++);
        stores.add(s2);

        Store s3 = new Store("야당점");
        s3.setId(nextId++);
        stores.add(s3);

        Store s4 = new Store("운정점");
        s4.setId(nextId++);
        stores.add(s4);

        Store s5 = new Store("명동점");
        s5.setId(nextId++);
        stores.add(s5);

        Store s6 = new Store("연남점");
        s6.setId(nextId++);
        stores.add(s6);

    }

    // CREATE : 지점 추가
    public Store save(Store store) {
        store.setId(nextId++);
        stores.add(store);
        return store;
    }

    // READ : 지점 전체 조회
    public List<Store> findAll() {
        return new ArrayList<>(stores);
    }

    // READ : 지점 id로 상세 조회
    public Optional<Store> findById(Integer id) {
        for (Store store : stores) {
            if (store.getId().equals(id)) {
                return Optional.of(store);
            }
        }
        return Optional.empty();
    }

    // UPDATE: 지점 수정
    public Store update(Store store) {
        for (Store s : stores) {
            if (s.getId().equals(store.getId())) {
                s.setName(store.getName());
                break;
            }
        }
        return store;
    }

    // DELETE : 지점 삭제
    public void deleteById(Integer id) {
        stores.removeIf(store -> store.getId().equals(id));
    }



}
