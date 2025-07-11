//plugins {
//    kotlin("jvm") version "1.9.25"
//    kotlin("plugin.spring") version "1.9.25"
//    id("org.springframework.boot") version "3.5.0"
//    id("io.spring.dependency-management") version "1.1.7"
//}
//
//group = "wsb.lisowski"
//version = "0.0.1-SNAPSHOT"
//
//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(21)
//    }
//}
//
//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
//    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
//    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
//    implementation("io.jsonwebtoken:jjwt:0.9.1")
//    implementation("javax.xml.bind:jaxb-api:2.3.1")
//    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//}
//
//kotlin {
//    compilerOptions {
//        freeCompilerArgs.addAll("-Xjsr305=strict")
//    }
//}
//
//tasks.withType<Test> {
//    useJUnitPlatform()
//}

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "wsb.lisowski"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test") // ← DODAJ TO!
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}