package lab.monilabexporterex.api

import lab.monilabexporterex.api.dto.GcMetricResponse
import lab.monilabexporterex.service.MetricService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gc")
class MetricController(
    private val metricService: MetricService,
) {
    @GetMapping("/metric")
    fun getGcMetrics(): ResponseEntity<List<GcMetricResponse>> {
        val data = metricService.getAllGcMetrics()
        return ResponseEntity.ok(data)
    }
}
