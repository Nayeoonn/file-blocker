package com.nayeon.fileblocker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayeon.fileblocker.domain.BlockedExtension;


// 커스텀 확장자 저장/조희/중복 검사용
public interface BlockedExtensionRepository extends JpaRepository<BlockedExtension, Long> {  
                                            // BlockExtension이라는 엔티티를 기본키 타입인 Long인 기준으로 DB에 자동으로 연결해주는 인터페이스
    
    // 동일한 확장자가 이미 등록되어 있는지 확인
    boolean existsByExtension(String extension);

    // 확장자 기준 조회(필요시)
    Optional<BlockedExtension> findByExtension(String extension);
    // Optional<T> : T객체가 있을수도 있고 없을 수도 있다는 것을 명시적으로 표현
}
