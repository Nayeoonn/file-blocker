package com.nayeon.fileblocker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nayeon.fileblocker.domain.BlockedExtension;
import com.nayeon.fileblocker.domain.FixedExtension;
import com.nayeon.fileblocker.service.ExtensionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/extensions")
@RequiredArgsConstructor
public class ExtensionController {
    
    private final ExtensionService extensionService;

    // 고정 확장자 전체 조회 - 체크박스 리스트 렌더링용
    @GetMapping("/fixed")
    public List<FixedExtension>getFixedExtensions() {
        log.info("[GET]: 고정 확장자 전체 조회 요청");
        return extensionService.getAllFixedExtensions();
    }

    //고정 확장자 상태 토글 - 프론트에서 체크박스를 클릭할 때 호출
    @PostMapping("/fixed/toggle")
    public void toggleFixedExtension(@RequestParam String ext){
        log.info("[POST]: 고정 확장자 상태 토글 요청 - ext: {}", ext);
        extensionService.toggleFixedExtensions(ext);
    }

    // 커스텀 확장자 전체 조회
    @GetMapping("/blocked")
    public List<BlockedExtension> getBlockedExtensions(){
        log.info("[GET]: 커스텀 확장자 전체 조회 요청");
        return extensionService.getAllBlockedExtensions();
    }
    
    // 확장자 추가
    @PostMapping("/blocked")
    public void addBlockedExtension(@RequestParam String ext){
        log.info("[POST]: 사용자가 확장자 추가 요청 - ext: {}", ext);
        extensionService.addBlockedExtension(ext);
    }

    //확장자 삭제
    @DeleteMapping("/blocked/{id}")
    public void deleteBlockedExtension(@PathVariable Long id){
        log.info("[DELETE]: 사용자가 확장자 삭제 요청 - id: {}", id);
        extensionService.removeBlockedExtension(id);
    }
}
