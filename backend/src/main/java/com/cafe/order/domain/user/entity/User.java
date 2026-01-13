package com.cafe.order.domain.user.entity;

import com.cafe.order.common.entity.BaseEntity;
import com.cafe.order.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // DB용 PK (자동 생성)

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId; // 로그인 ID

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // ADMIN, CUSTOMER용 생성자
    public User(String loginId, String password, String name, UserRole role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    // SELLER용 생성자
    public User(String loginId, String password, String name, UserRole role, Store store) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
        this.store = store;
    }
}
