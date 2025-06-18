package com.nayeon.fileblocker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nayeon.fileblocker.domain.BlockedExtension;
import com.nayeon.fileblocker.domain.FixedExtension;
import com.nayeon.fileblocker.repository.BlockedExtensionRepository;
import com.nayeon.fileblocker.repository.FixedExtensionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor        // final 또는 @NonNull 필드만 파라미터로 받는 생성자
@Transactional(readOnly = true) // 조회할 때만 사용해서 성능 최적화
public class ExtensionService {
    
    private final FixedExtensionRepository fixedExtensionRepository;
    private final BlockedExtensionRepository blockedExtensionRepository;

    
    // 고정 확장자 전체 조회
    public List<FixedExtension> getAllFixedExtensions() {
        return fixedExtensionRepository.findAll();
    }


    // 고정 확장자 체크 상태 토글 (ON ↔ OFF)
    // 해당 확장자가 없을 경우
    @Transactional
    public void toggleFixedExtensions(String extension) {
        FixedExtension fixedExtension = fixedExtensionRepository.findByExtension(extension)
            .orElseThrow(() -> new IllegalArgumentException("해당 확장자를 찾을 수 없습니다."));
        fixedExtension.toggleChecked(); // ✅ 오타 수정
    }


    // 사용자 차단 확장자 추가
    // 이미 등록된 확장자인 경우
    @Transactional
    public void addBlockedExtension(String extension) {
        if (blockedExtensionRepository.findByExtension(extension).isPresent()) {
            throw new IllegalArgumentException("이미 차단된 확장자입니다."); // ✅ 예외 수정
        }

        BlockedExtension blockedExtension = new BlockedExtension(extension);
        blockedExtensionRepository.save(blockedExtension);
    }


    // 사용자 차단 확장자 삭제
    @Transactional
    public void removeBlockedExtension(Long id) { 
        blockedExtensionRepository.deleteById(id);
    }


    // 사용자 확장자 중복 여부 확인
    public boolean isBlockedExtensionExist(String extension) {
        return blockedExtensionRepository.findByExtension(extension).isPresent();
    }
}
