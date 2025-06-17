package com.nayeon.fileblocker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayeon.fileblocker.domain.FixedExtension;

// 시스템에서 기본으로 제공하는 확장자의 상태를 저장/조회
public interface FixedExtensionRepository extends JpaRepository<FixedExtension, Long>{
    
    // 확장자 중복체크 (초기 설정 or 체크박스 상태 업데이트시 사용)
    // 해당 확장자가 존재하면 Optional로 감싸진 엔티티 반환
    Optional<FixedExtension> findByExtension(String extension);
}
