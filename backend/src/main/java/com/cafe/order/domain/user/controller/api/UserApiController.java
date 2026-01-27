package com.cafe.order.domain.user.controller.api;

import com.cafe.order.domain.store.dto.StoreResponse;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.user.dto.SellerCreateRequest;
import com.cafe.order.domain.user.dto.SellerDto;
import com.cafe.order.domain.user.dto.SellerUpdateRequest;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/sellers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserApiController {

    private final UserService userService;
    private final StoreService storeService;

    /**
     * 판매자 계정 생성 시 선택 가능한(판매자 배정이 안된) 지점 목록 조호
     */
    @GetMapping("/available-stores")
    public ResponseEntity<List<StoreResponse>> getAvailableStores() {
        // 1. 이미 판매자가 배정된 지점 ID 목록 조회
        List<Integer> assignedStores = userService.getAssignedStoreIds();

        // 2.배정 가능한 지점 엔티티 조회
        List<Store> availableStores = storeService.findAvailableStores(assignedStores);

        // 3. DTO로 변환하여 반환
        List<StoreResponse> response = availableStores.stream()
                .map(StoreResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * 판매자 계정 생성
     */
    @PostMapping
    public ResponseEntity<SellerDto> createSeller(@Valid @RequestBody SellerCreateRequest request) {
        User user = userService.create(
                request.getLoginId(),
                request.getPassword(),
                request.getName(),
                request.getStoreId()
        );

        return ResponseEntity.ok(new SellerDto(user));
    }

    /**
     * 판매자 계정 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<SellerDto>> getAllSellers() {
        return ResponseEntity.ok(userService.findAllSellerWithStoreName());
    }

    /**
     * 판매자 계정 수정
     */
    @PatchMapping("/{sellerId}")
    public ResponseEntity<SellerDto> updateSeller(
            @PathVariable Integer sellerId,
            @RequestBody SellerUpdateRequest request) {

        User updateUser = userService.update(
                sellerId,
                request.getPassword(),
                request.getName(),
                request.getStoreId()
        );

        return ResponseEntity.ok(new SellerDto(updateUser));
    }

    /**
     * 판매자 계정 삭제
     */
    @DeleteMapping("/{sellerId}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Integer sellerId) {
        userService.delete(sellerId);

        return ResponseEntity.noContent().build();
    }
}

