package lab.`ai-model`.gc

data class GcTrainData(
    val count: Long,
    val time: Long,
    val pause: Long,
    val allocationRate: Double,
    val liveDataSize: Long,
    val gcStrategy: String,
    val label: Int, // 1 정상, 0 비정상
)
