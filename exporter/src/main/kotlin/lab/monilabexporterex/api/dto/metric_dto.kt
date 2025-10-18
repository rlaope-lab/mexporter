package lab.monilabexporterex.api.dto

data class GcMetricResponse(
    val id: Long,
    val label: String,
    val count: Long,
    val time: Long,
    val pause: Long,
    val allocationRate: Double,
    val liveDataSize: Long,
    val gcStrategy: String,
)
