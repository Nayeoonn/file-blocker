# JPA Repository 메서드 정리

## 기본 제공 메서드
- `save(entity)`        : 저장 또는 수정
- `findAll()`           : 전체 조회
- `findById(id)`        : ID 기준 조회 (`Optional<T>` 반환)
- `delete(entity)`      : 삭제
- `existsBy필드명(value)` : 해당 필드 기준 존재 여부 확인


## Optional 주요 메서드
- `isPresent()`     : 값이 존재하는지 확인
- `get()`           : 값 꺼내기 (값 없으면 예외 발생)
- `orElse(default)` : 없으면 기본값
- `orElseThrow()`   : 없으면 예외 던지기