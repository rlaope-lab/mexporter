package lab.monilabexporterex.api

import lab.monilabexporterex.exporter.JvmExporter
import org.springframework.web.bind.annotation.RestController

@RestController
class ExporterController(
    private val jvmExporter: JvmExporter
) {
}