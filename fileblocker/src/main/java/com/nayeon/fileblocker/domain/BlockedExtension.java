package com.nayeon.fileblocker.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA가 이 클래스를 DB 테이블로 인식
@Table(name = "blocked_extension")
@Getter
@NoArgsConstructor      // 파라미터가 없는 기본 생성자를 자동으로 만들어주는 Lombok 어노테이션
public class BlockedExtension {
    
    // @Id + @GeneratedValue -> 기본키 자동 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoinvrement
    private Long id;        // -> 기본키

    // 문자열 길이 varchar(20), null 허용 X, 증복금지
    @Column(length = 20, nullable = false, unique = true)
    private String extension;

    private LocalDateTime createdAt;

    public BlockedExtension(String extension){
        this.extension = (extension.toLowerCase());    // 중복 방지를 위해 소문자로 통일
        this.createdAt = LocalDateTime.now();
    }

}
