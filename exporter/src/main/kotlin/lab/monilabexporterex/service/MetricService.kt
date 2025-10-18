package lab.monilabexporterex.service

import lab.monilabexporterex.api.dto.GcMetricResponse
import lab.monilabexporterex.model.GcEntity
import lab.monilabexporterex.repository.GcRepository
import org.springframework.stereotype.Service

@Service
class MetricService(
    private val gcRepository: GcRepository,
) {
    fun getAllGcMetrics(): List<GcMetricResponse> {
        return gcRepository.findAll().map { it.toResponse() }
    }

    private fun GcEntity.toResponse(): GcMetricResponse {
        return GcMetricResponse(
            id = this.id,
            label = this.label.name,
            count = this.count,
            time = this.time,
            pause = this.pause,
            allocationRate = this.allocationRate,
            liveDataSize = this.liveDataSize,
            gcStrategy = this.gcStrategy,
        )
    }
}
