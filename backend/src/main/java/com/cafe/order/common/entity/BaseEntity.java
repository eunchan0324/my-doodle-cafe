package com.cafe.order.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 상속받는 자식 클래스에게 매핑 정보(컬럼)만 제공함
@EntityListeners(AuditingEntityListener.class) // 엔티티의 변화를 감지하는 리스너 설정
@Getter
public abstract class BaseEntity { // 추상 클래스로 만들기 (단독으로 쓸 일 없음)

    @CreatedDate // 데이터 생성 시 시간 자동 저장
    @Column(updatable = false) // 생성일은 수정되면 안 됨
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터 수정 시 시간 자동 업데이트
    private LocalDateTime updatedAt;

    @CreatedBy // 생성자 자동 저장
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy // 수정자 자동 저장
    private String updatedBy;
}
