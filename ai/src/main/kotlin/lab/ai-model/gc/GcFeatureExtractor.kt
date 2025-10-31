package lab.`ai-model`.gc

object GcFeatureExtractor {
    private val gcStrategyMap =
        mapOf(
            "G1" to 0.0,
            "Parallel" to 1.0,
            "Serial" to 2.0,
            "ZGC" to 3.0,
        )

    fun extract(gc: GcTrainData): DoubleArray {
        return doubleArrayOf(
            gc.count.toDouble(),
            gc.time.toDouble(),
            gc.pause.toDouble(),
            gc.allocationRate,
            gc.liveDataSize.toDouble(),
            gcStrategyMap[gc.gcStrategy] ?: -1.0,
        )
    }
}
