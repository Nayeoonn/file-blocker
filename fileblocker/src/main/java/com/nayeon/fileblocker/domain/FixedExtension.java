package com.nayeon.fileblocker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fixed_extension")
@Getter
@NoArgsConstructor
public class FixedExtension {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String extension;

    // 차단 여부 true  : 차단된 상태 (checkbox checked)
    //         false : 허용된 상태
    @Column(nullable = false)
    private boolean checked;

    // 확장자명과 차단 여부를 전달받기 위한 생성자
    public FixedExtension(String extension, boolean checked) {
        this.extension = extension.toLowerCase(); 
        this.checked = checked;
    }

    // 외부에서 상태만 바꿀 수 있도록 허용
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    // 체크 여부를 반전시킴 (true → false, false → true)
    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
