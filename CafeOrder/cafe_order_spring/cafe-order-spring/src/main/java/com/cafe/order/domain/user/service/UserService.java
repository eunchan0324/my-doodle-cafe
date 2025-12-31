package com.cafe.order.domain.user.service;

import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.user.dto.UserSignupRequest;
import com.cafe.order.domain.user.repo.JpaUserRepository;
import com.cafe.order.domain.user.dto.SellerDto;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회용 트랜잭션으로 설정 (성능 최적화)
public class UserService {

    private final JpaUserRepository userRepository;
//    private final SqlUserRepository userRepository;
//    private final InMemoryUserRepository userRepository;

    private final StoreService storeService;
    private final PasswordEncoder passwordEncoder;


    /**
     * 구매자(Customer) 기능
     */

    /**
     * 회원가입
     */
    @Transactional
    public void signup(UserSignupRequest request) {
        // 1. 아이디 중복 체크
        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 유저 엔티티 생성 (Role = CUSTOMER, StorieId = null)
        User user = new User(
                request.getLoginId(),
                encodedPassword,
                request.getName(),
                UserRole.CUSTOMER,
                null
        );

        // 4. DB 저장
        userRepository.save(user);
    }


    /**
     * 판매자(Seller) 및 관리자 기능
     */
    // READ : 전체 판매자 계정 조회
    public List<User> findAllSellers() {
        return userRepository.findByRole(UserRole.SELLER);
    }

    // READ : 판매자 목록 (지점 이름 포함) (stream 람다식)
    public List<SellerDto> findAllSellerWithStoreName() {
        List<User> sellers = userRepository.findByRole(UserRole.SELLER);

        return sellers.stream()
                .map(seller -> {
                    String storeName = storeService.findById(seller.getStoreId()).getName();
                    return new SellerDto((seller), storeName);
                })
                .collect(Collectors.toList());
    }

    //  READ : 판매자 목록 (지점 이름 포함) (원시 자바 반복문)
//  public List<SellerDto> findAllSellerWithStoreName() {
//    List<User> sellers = userRepository.findByRole(UserRole.SELLER);
//
//    List<SellerDto> sellerDtos = new ArrayList<>();
//
//    for (User seller : sellers) {
//      String storeName = storeService.findById(seller.getStoreId()).getName();
//      sellerDtos.add(new SellerDto(seller, storeName));
//    }
//
//    return sellerDtos;
//  }

    // READ : 판매자 ID로 판매자 조회
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // CREATE : 판매자 계정 생성
    @Transactional
    public User create(String username, String password, String name, Integer storeId) {
        String encoded = passwordEncoder.encode(password);

        User user = new User(username, encoded, name, UserRole.SELLER, storeId);
        return userRepository.save(user);
    }

    // UPDATE : 판매자 계정 수정
    public User update(Integer id, String password, String name, Integer storeId) {
        User seller = userRepository.findById(id).orElse(null);

        if (seller == null) {
            throw new IllegalArgumentException("판매자를 찾을 수 없습니다.");
        }

        // 비밀번호가 입력된 경우만 변경
        if (password != null && !password.isEmpty()) {
            seller.setPassword(passwordEncoder.encode(password)); // 암호화 적용
        }

        seller.setName(name);
        seller.setStoreId(storeId);

        return userRepository.save(seller); // JPA
//        return userRepository.update(seller); // SQL, InMemory
    }

    // DELETE : 판매자 계정 삭제
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }


    // 이미 배정된 지점 ID 목록 (람다식)
    public List<Integer> getAssignedStoreIds() {
        return userRepository.findByRole(UserRole.SELLER).stream()
                .map(User::getStoreId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }


    // 이미 배정된 지점 ID 목록 (자바 반복문 코드)
//  public List<Integer> getAssignedStoreIds() {
//    List<User> sellers = userRepository.findByRole(UserRole.SELLER);
//
//    List<Integer> storeIds = new ArrayList<>();
//
//    for (User seller : sellers) {
//      if (seller.getStoreId() != null) {
//        if (!storeIds.contains(seller.getStoreId())) {
//          storeIds.add(seller.getStoreId());
//        }
//      }
//    }
//
//    return storeIds;
//  }


    // 특정 판매자를 제외한 배정된 지점 ID 목록 (stream 람다식)
    public List<Integer> getAssignedStoreIdsExcept(Integer excludeSellerId) {
        return userRepository.findByRole(UserRole.SELLER).stream()
                .filter(seller -> !seller.getId().equals(excludeSellerId))
                .map(User::getStoreId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    // 특정 판매자를 제외한 배정된 지점 ID 목록 (원시 자바 반목문 코드)
//  public List<Integer> getAssignedStoreIdsExcept(Integer excludeSellerId) {
//    List<User> sellers = userRepository.findByRole(UserRole.SELLER);
//
//    List<Integer> storeIds = new ArrayList<>();
//
//    for (User seller : sellers) {
//      if (!seller.getId().equals(excludeSellerId)) {
//        Integer storeId = seller.getStoreId();
//
//        if (storeId != null) {
//          if (!storeIds.contains(storeId)) {
//            storeIds.add(storeId);
//          }
//        }
//      }
//    }
//    return storeIds;
//  }


}

