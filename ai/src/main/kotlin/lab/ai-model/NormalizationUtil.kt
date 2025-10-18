package lab.`ai-model`

object NormalizationUtil {
    fun normalize(features: Array<DoubleArray>): Array<DoubleArray> {
        val numFeatures = features.first().size
        val minVals = DoubleArray(numFeatures) { idx -> features.minOf { it[idx] } }
        val maxVals = DoubleArray(numFeatures) { idx -> features.maxOf { it[idx] } }

        return features.map { f ->
            DoubleArray(numFeatures) { i ->
                if (maxVals[i] == minVals[i]) 0.0
                else (f[i] - minVals[i]) / (maxVals[i] - minVals[i])
            }
        }.toTypedArray()
    }
}
