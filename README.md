# 📁 파일 확장자 차단 시스템

Spring Boot 기반의 확장자 차단 웹 애플리케이션  
사용자는 고정 확장자 및 커스텀 확장자를 선택하거나 추가하여, 특정 형식의 파일 첨부 또는 전송 제한

---

## ✅ 주요 기능

- 고정 확장자 목록 제공 (예: `.exe`, `.js`, `.bat` 등)
- 고정 확장자 체크/해제 상태 서버에 저장
- 사용자 정의 확장자 추가/삭제 기능
- 차단된 확장자 목록 조회 및 화면 표시
- REST API 기반 클라이언트-서버 통신
- 간단한 HTML + JS 기반 프론트엔드 (`index.html`)

---

## 🛠 기술 스택

| 영역 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot |
| DB | H2 (in-memory) |
| Frontend | HTML / CSS / JavaScript |
| Build Tool | Gradle |
| 배포 | Render (Dockerfile 기반) |

---

## 🔗 API 명세 

### 고정 확장자 관련

- `GET /api/extensions/fixed`  
  → 고정 확장자 목록 조회

- `POST /api/extensions/fixed/toggle?ext=xxx`  
  → 확장자 체크/해제 상태 토글

---

### 커스텀 확장자 관련

- `GET /api/extensions/blocked`  
  → 사용자가 등록한 확장자 전체 조회

- `POST /api/extensions/blocked?ext=xxx`  
  → 커스텀 확장자 추가

- `DELETE /api/extensions/blocked/{id}`  
  → 특정 확장자 삭제

---

## 💡 개선 사항 및 향후 발전 방향

### 1. 중복 확장자 처리 강화
- 현재는 프론트에서만 중복 검사를 진행 중
- 여러 사용자가 동시에 같은 확장자를 추가하면 중복 저장될 가능성 ⭕️
- 개선 방안:
  - 서버 측 중복 검사 로직 추가 
  - DB 컬럼에 `UNIQUE` 제약 설정

### 2. 실시간 데이터 동기화
- 현재는 새로고침해야 다른 사용자의 변경 사항이 반영
- WebSocket, SSE, Polling 등을 활용하면 실시간 반영 가능

### 3. 데이터 영속성 문제
- H2 인메모리 DB(`mem:testdb`) 사용 중이므로 Render에서 서버가 슬립되면 데이터가 초기화
- 개선 방안:
  - H2 File 기반 전환
  - 외부 RDB(PostgreSQL 등) 연동

---

## 🧱 배포 구성 및 오류 해결

### 1. 기본 배포 구성

- Render 플랫폼 사용
- Spring Boot 앱 + Gradle 빌드 + Dockerfile 사용
- 리포지토리 루트 구조:

```
📦 file-blocker/
├── Dockerfile
├── build.gradle
├── settings.gradle
└── src/...
```

---

### 2. 배포 중 발생한 오류 및 해결 방법

#### ① 오류: Dockerfile not found

```
오류 메시지:
error: failed to solve: failed to read dockerfile: open Dockerfile: no such file or directory

원인:
- Render는 기본적으로 Dockerfile이 루트 경로에 있다고 가정함
- Dockerfile이 올바른 위치에 있어도 COPY 경로나 경로 인식 오류로 인해 실패할 수 있음
- 또는 Render 대시보드에서 Dockerfile 경로를 명시하지 않아 자동 인식에 실패할 수 있음

해결 방법:
- Dockerfile을 루트(/) 경로에 위치시켜봄
- Dockerfile 위치는 다시 그대로 유지하고
- Render 대시보드에서 다음 설정을 명시하여 정상 작동시킴:
  - Root Directory: fileblocker
  - Dockerfile Path: ./Dockerfile
  - Docker Build Context Directory: .
```

---

#### ② 오류: gradle bootJar 실행 실패

```
오류 메시지:
Directory '/app' does not contain a Gradle build.
A Gradle build should contain a 'settings.gradle' or 'build.gradle'

원인:
Dockerfile에서 복사한 경로(/app)에 Gradle 설정 파일이 없다고 인식됨

해결 방법:
- COPY 경로를 `. /app` 으로 설정
- Dockerfile과 `build.gradle`, `settings.gradle`이 같은 디렉토리에 있어야 정상 작동
```

---

#### ③ 슬립 모드 관련

```
현상:
- 배포 후 주소 접속 시 첫 요청이 오래 걸림

원인:
- Render Free 플랜은 15분 동안 미사용 시 슬립 모드 진입

해결 방법:
- 구조적 해결은 불가 (Free 플랜의 기본 정책)
- 안내 문구를 README에 명시하거나 유료 플랜 전환 고려
```

---

## 👩🏻‍💻 실행 방법

### 1. 로컬 실행

```bash
./gradlew bootRun

# 기본 포트: http://localhost:8080
# 정적 페이지 위치: src/main/resources/static/index.html
```

### 2. 배포 주소

👉 https://file-blocker.onrender.com
