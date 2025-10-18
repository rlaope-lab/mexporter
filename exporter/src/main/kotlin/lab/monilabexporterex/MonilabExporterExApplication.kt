package lab.monilabexporterex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MonilabExporterExApplication

fun main(args: Array<String>) {
    runApplication<MonilabExporterExApplication>(*args)
}
