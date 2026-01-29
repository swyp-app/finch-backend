plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.spring") version "2.2.20" // Kotlin 클래스에 자동으로 open 키워드를 추가 해줌

    id("org.springframework.boot") version "3.2.1" // 실행 가능한 Jar 빌드 및 Boot 환경 제공
    id("io.spring.dependency-management") version "1.1.4" // 라이브러리 버전 자동 관리
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}