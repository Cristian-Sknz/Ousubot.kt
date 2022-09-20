import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "me.sknz"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

extra["springCloudVersion"] = "2021.0.4"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Framework
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // FeignClient
    implementation("io.github.openfeign:feign-jackson:11.9.1")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Jackson DataBind JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.4")


    // Temporary Database
    runtimeOnly("com.h2database:h2")

    // JDA Discord API
    implementation("net.dv8tion:JDA:5.0.0-alpha.19")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

configurations {
    all {
        exclude(group = "net.dv8tion", module = "opus-java")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}