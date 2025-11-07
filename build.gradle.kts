import org.gradle.internal.impldep.org.eclipse.jgit.revwalk.filter.RevFilter.MERGE_BASE
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "3.5.5" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    id("java")
}

allprojects {
    group = "lab"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "java")

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.register("findBuildModule") {
    group = "build analysis"
    description = "Detect changed modules among allowedBuildModules only"

    val allowedBuildModules = listOf(
        "ai",
        "exporter",
        "cli"
    )

    doLast {
        val branchNameOut = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
            standardOutput = branchNameOut
        }
        val currentBranch = branchNameOut.toString().trim()
        println("🔍 현재 브랜치: $currentBranch")

        val output = ByteArrayOutputStream()
        exec {
            commandLine("bash", "-c", """
                git fetch origin master;
                MERGE_BASE=$(git merge-base HEAD origin/master);
                (git diff --name-only $MERGE_BASE HEAD || true);
                (git diff --cached --name-only || true);
                (git diff --name-only || true)
            """.trimIndent())
            standardOutput = output
        }

        val changedFiles = output.toString().trim().lines().filter { it.isNotBlank() }
        if (changedFiles.isEmpty()) {
            println("✅ 변경된 파일이 없습니다.")
            return@doLast
        }

        val changedModules = mutableSetOf<String>()

        rootProject.subprojects.forEach { project ->
            val projectPath = project.projectDir.toPath().toAbsolutePath().normalize().toString()
            val rootPath = rootProject.projectDir.toPath().toAbsolutePath().normalize().toString()
            val relPath = projectPath.replace("$rootPath${File.separator}", "").replace('\\', '/')

            changedFiles.forEach { file ->
                if (file.startsWith("$relPath/") && allowedBuildModules.contains(project.name)) {
                    changedModules += project.name
                }
            }
        }

        println("")
        println("🟨 감지된 빌드 대상 모듈 (allowedBuildModules 한정)")
        if (changedModules.isEmpty()) {
            println(" (없음)")
        } else {
            changedModules.forEach { println(" - $it") }
        }

        println("")
        println("[참고] master와 diff 기준으로 감지되므로, 브랜치가 오래되면 결과 정확도가 떨어질 수 있습니다.")
    }
}
