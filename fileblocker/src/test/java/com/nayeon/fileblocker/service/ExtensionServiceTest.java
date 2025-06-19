package com.nayeon.fileblocker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.nayeon.fileblocker.domain.BlockedExtension;
import com.nayeon.fileblocker.domain.FixedExtension;
import com.nayeon.fileblocker.repository.BlockedExtensionRepository;
import com.nayeon.fileblocker.repository.FixedExtensionRepository;

@SpringBootTest 
@Transactional // 각 테스트 후 데이터베이스 롤백하여 깨끗한 상태 유지
public class ExtensionServiceTest {

    @Autowired
    private ExtensionService extensionService; 

    @Autowired
    private BlockedExtensionRepository blockedExtensionRepository; 

    @Autowired
    private FixedExtensionRepository fixedExtensionRepository; 

    @Test
    void testGetAllFixedExtensions() {
        // 고정 확장자 전체 조회
        List<FixedExtension> fixedExtensions = extensionService.getAllFixedExtensions();

        // 결과가 null이 아니어야 함
        assertThat(fixedExtensions).isNotNull();
    }

    @Test
    void testGetAllBlockedExtensions() {
        // 추가한 확장자 전체 조회
        List<BlockedExtension> blockedExtensions = extensionService.getAllBlockedExtension();

        assertThat(blockedExtensions).isNotNull();
    }
    @Test
    void testToggleFixedExtension() {
        // 테스트용 고정 확장자 저장
        FixedExtension ext = new FixedExtension("jpg", false);
        fixedExtensionRepository.save(ext);
        boolean before = ext.isChecked();

        // 상태 토글
        extensionService.toggleFixedExtensions("jpg");

        // 상태가 반대로 변경되었는지 확인
        FixedExtension updated = fixedExtensionRepository.findByExtension("jpg").get();
        assertThat(updated.isChecked()).isNotEqualTo(before);
    }

    @Test
    void testToggleFixedExtensionThrowsWhenNotFound() {
        // 존재하지 않는 확장자 토글 시 예외 발생해야 함
        assertThrows(IllegalArgumentException.class, () -> {
            extensionService.toggleFixedExtensions("not_exist");
        });
    }

    @Test
    void testAddBlockedExtension() {
        // 테스트용 확장자
        String extension = "test";

        // 차단 확장자 추가
        extensionService.addBlockedExtension(extension);

        // 저장 여부 확인
        assertThat(extensionService.isBlockedExtensionExist(extension)).isTrue();
    }

    @Test
    void testDuplicateBlockedExtensionThrowsException() {
        // 동일한 확장자 두 번 등록 시도
        String extension = "duplicate";
        extensionService.addBlockedExtension(extension);

        // 중복 등록 시 예외 발생
        assertThrows(IllegalArgumentException.class, () -> {
            extensionService.addBlockedExtension(extension);
        });
    }

    @Test
    void testRemoveBlockedExtension() {
        // 차단 확장자 등록 후 ID 확인
        BlockedExtension blocked = new BlockedExtension("delete");
        blockedExtensionRepository.save(blocked);
        Long id = blocked.getId();

        // 해당 ID로 삭제
        extensionService.removeBlockedExtension(id);

        // DB에서 더 이상 존재하지 않아야 함
        assertThat(blockedExtensionRepository.findById(id)).isEmpty();
    }
}
