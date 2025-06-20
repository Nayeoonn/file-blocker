package com.nayeon.fileblocker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nayeon.fileblocker.domain.BlockedExtension;
import com.nayeon.fileblocker.domain.FixedExtension;
import com.nayeon.fileblocker.repository.BlockedExtensionRepository;
import com.nayeon.fileblocker.repository.FixedExtensionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor        // final 또는 @NonNull 필드만 파라미터로 받는 생성자
@Transactional(readOnly = true) // 조회할 때만 사용해서 성능 최적화
public class ExtensionService {
    
    private final FixedExtensionRepository fixedExtensionRepository;
    private final BlockedExtensionRepository blockedExtensionRepository;

    
    // 고정 확장자 전체 조회
    public List<FixedExtension> getAllFixedExtensions() {
        log.info("고정된 확장자 전체 조회 요청");
        return fixedExtensionRepository.findAll();
    }

    public List<BlockedExtension> getAllBlockedExtensions(){
        log.info("사용자가 추가한 확장자 전체 조회 요청");
        return blockedExtensionRepository.findAll();
    }


    // 고정 확장자 체크 상태 토글 (ON ↔ OFF)
    // 해당 확장자가 없을 경우
    @Transactional
    public void toggleFixedExtensions(String extension) {
        log.info("상태 토글 시도: {}", extension);
        FixedExtension fixedExtension = fixedExtensionRepository.findByExtension(extension)
            .orElseThrow(() -> {
                log.warn("존재하지 않느 고정 확장자: {}", extension);
                return new IllegalArgumentException("해당 확장자를 찾을 수 없습니다.");
        });
        fixedExtension.toggleChecked(); // 오타 수정
        log.info("토글 완료: {}", extension);
    }


    // 사용자가 차단할 확장자 추가
    // 이미 등록된 확장자인 경우
    @Transactional
    public void addBlockedExtension(String extension) {
        log.info("커스텀 확장자 등록 시도: {}", extension);

        if (blockedExtensionRepository.findByExtension(extension).isPresent()) {
            log.warn("중복 확장자 등록 시도: {}", extension);
            throw new IllegalArgumentException("이미 차단된 확장자입니다."); 
        }

        BlockedExtension blockedExtension = new BlockedExtension(extension);
        blockedExtensionRepository.save(blockedExtension);
        log.info("커스텀 확장자 등록 완료: {}", extension);
    }


    // 사용자 커스텀 확장자 삭제
    @Transactional
    public void removeBlockedExtension(Long id) { 
        log.info("커스텀 확장자 삭제 시도 - ID: {}", id);

        blockedExtensionRepository.deleteById(id);
        log.info("삭제 완료 - ID: {}", id);
    }


    // 사용자 확장자 중복 여부 확인
    public boolean isBlockedExtensionExist(String extension) {
        boolean exists = blockedExtensionRepository.findByExtension(extension).isPresent();
        log.debug("중복 확인 - {} → {}", extension, exists);
        return exists;
    }
}
