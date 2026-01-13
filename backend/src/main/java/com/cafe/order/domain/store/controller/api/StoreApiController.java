package com.cafe.order.domain.store.controller.api;

import com.cafe.order.domain.store.dto.StoreCreateRequest;
import com.cafe.order.domain.store.dto.StoreResponse;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Store 관련 REST API를 제공하는 컨트롤러
 * <p>
 * @RestController: @Controller + @ResponseBody. 모든 메서드의 반환값이 JSON으로 직렬화됩니다.
 * @RequestMapping("/api/v1/stores"): API 버전 관리를 위해 v1 경로를 포함하는 것이 관례입니다.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreApiController {

    private final StoreService storeService;

    /**
     * 전체 지점 목록 조회
     * GET /api/v1/stores
     */
    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> stores = storeService.findAll().stream()
                .map(StoreResponse::from) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(stores); // 200 OK 상태 코드와 함께 데이터 반환
    }

    /**
     * 지점 상세 조회
     * GET /api/v1/stores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable Integer id) {
        Store store = storeService.findById(id);
        if (store == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        return ResponseEntity.ok(StoreResponse.from(store));
    }

    /**
     * 지점 생성
     * POST /api/v1/stores
     * @Valid: StoreCreateRequest의 @NotBlank 검증을 활성화합니다.
     */
    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody StoreCreateRequest request) {
        // @RequestBody: JSON 데이터를 자바 객체로 변환해줍니다.
        Store savedStore = storeService.create(request.getName());
        
        // 생성 시에는 201 Created 응답을 주는 것이 REST 원칙에 가깝습니다.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(StoreResponse.from(savedStore));
    }

    /**
     * 지점 수정
     * PUT /api/v1/stores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable Integer id,
            @Valid @RequestBody StoreCreateRequest request) {
        
        Store updatedStore = storeService.update(id, request.getName());
        return ResponseEntity.ok(StoreResponse.from(updatedStore));
    }

    /**
     * 지점 삭제
     * DELETE /api/v1/stores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer id) {
        storeService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content (성공했지만 줄 데이터는 없음)
    }
}
