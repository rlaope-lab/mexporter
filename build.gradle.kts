import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject
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

abstract class FindBuildModuleTask @Inject constructor(
    private val execOps: ExecOperations
) : DefaultTask() {

    @TaskAction
    fun run() {
        val allowedBuildModules = listOf(
            "exporter",
            "ai",
            "cli"
        )

        fun execAndCapture(vararg cmd: String): String {
            val output = ByteArrayOutputStream()
            execOps.exec {
                commandLine(*cmd)
                standardOutput = output
                errorOutput = ByteArrayOutputStream()
                isIgnoreExitValue = true
            }
            return output.toString().trim()
        }

        val currentBranch = execAndCapture("git", "rev-parse", "--abbrev-ref", "HEAD")
        println("🔍 현재 브랜치: $currentBranch")

        execOps.exec {
            commandLine("git", "fetch", "origin", "master")
            isIgnoreExitValue = true
        }

        val mergeBaseOutput = ByteArrayOutputStream()
        execOps.exec {
            commandLine("git", "merge-base", "HEAD", "origin/master")
            standardOutput = mergeBaseOutput
            isIgnoreExitValue = true
        }
        val mergeBase = mergeBaseOutput.toString().trim()

        if (mergeBase.isBlank()) {
            println("⚠️ merge-base를 찾을 수 없습니다. 브랜치 동기화 상태를 확인하세요.")
            return
        }

        val diffOutput = ByteArrayOutputStream()
        execOps.exec {
            commandLine(
                "bash", "-c", """
                    git diff --name-only $mergeBase..HEAD
                    git diff --cached --name-only
                    git diff --name-only
                """.trimIndent()
            )
            standardOutput = diffOutput
            isIgnoreExitValue = true
        }

        val changedFiles = diffOutput.toString().trim().lines().filter { it.isNotBlank() }

        if (changedFiles.isEmpty()) {
            println("✅ 변경된 파일이 없습니다.")
            return
        }

        val changedModules = mutableSetOf<String>()
        val rootPath = project.rootDir.absolutePath

        project.rootProject.subprojects.forEach { sub ->
            val relPath = sub.projectDir.absolutePath
                .replace(rootPath + File.separator, "")
                .replace("\\", "/")

            changedFiles.forEach { file ->
                if (file.startsWith("$relPath/") && allowedBuildModules.contains(sub.name)) {
                    changedModules += sub.name
                }
            }
        }

        println()
        println("🟨 감지된 빌드 대상 모듈 (exporter, ai, cli 한정)")
        if (changedModules.isEmpty()) {
            println(" (없음)")
        } else {
            changedModules.forEach { println(" - $it") }
        }

        println()
        println("[참고] master와 diff 기준으로 감지되므로, 브랜치가 오래되면 결과 정확도가 떨어질 수 있습니다.")
    }
}

tasks.register<FindBuildModuleTask>("findBuildModule") {
    group = "build analysis"
    description = "Detect changed modules among exporter, ai, cli only"
}

