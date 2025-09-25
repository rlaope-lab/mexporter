plugins {
    kotlin("jvm")
    application
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(project(":exporter"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    // CLI 엔트리포인트
    mainClass.set("lab.cli.MainKt")
}

ktlint {
    filter {
        exclude { entry -> entry.file.path.contains("src/test/") }
    }
}

tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask> {
    if (name.contains("TestSourceSet")) {
        enabled = false
    }
}

tasks.named("ktlintCheck") {
    enabled = false
}
tasks.named("ktlintMainSourceSetCheck") {
    enabled = false
}
