package lab.monilabexporterex.api

import lab.monilabexporterex.exporter.jvm.application.DefaultApplicationExporter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(private val defaultApplicationExporter: DefaultApplicationExporter) {
    @GetMapping("/health")
    fun healthCheck(): String {
        return "OK"
    }
}
