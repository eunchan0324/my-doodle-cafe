package com.cafe.order.domain.store.dto;

import com.cafe.order.domain.store.entity.Store;
import lombok.Getter;

/**
 * 클라이언트에게 가게 정보를 반환할 때 사용하는 DTO
 * <p>
 * 역할:
 * 1. 엔티티(Store)의 데이터를 필요한 부분만 복사해서 전달합니다.
 * 2. 엔티티 내부 구조가 변경되어도 API 응답 스펙을 유지할 수 있게 해줍니다.
 * </p>
 */
@Getter
public class StoreResponse {

    private final Integer id;
    private final String name;

    // 생성자를 private으로 막고, static 팩토리 메서드(from)를 사용하는 패턴을 자주 씁니다.
    private StoreResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 엔티티(Store)를 DTO(StoreResponse)로 변환하는 정적 메서드입니다.
     * <p>
     * 장점:
     * 1. 변환 로직을 한 곳에서 관리할 수 있습니다.
     * 2. 스트림(Stream) API에서 메서드 레퍼런스(StoreResponse::from)로 사용하기 좋습니다.
     * </p>
     */
    public static StoreResponse from(Store store) {
        return new StoreResponse(
            store.getId(),
            store.getName()
        );
    }
}
