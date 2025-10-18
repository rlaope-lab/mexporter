package lab.`ai-model`.gc

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import smile.classification.LogisticRegression
import smile.data.DataFrame
import smile.data.formula.Formula
import smile.data.vector.DoubleVector
import smile.data.vector.IntVector
import java.io.File
import java.io.ObjectOutputStream
import java.util.*

@Component
class GcTrainer {
    private val extractor: GcFeatureExtractor by lazy { GcFeatureExtractor }
    private lateinit var model: LogisticRegression
    private val log = LoggerFactory.getLogger(GcTrainer::class.java)

    private val projectRootDir: String = System.getProperty("user.dir")
    private val modelDir = File("$projectRootDir/ai-models/gc-model").apply { mkdirs() }

    fun train() {
        log.info("Start GcTrainer training...")
        val dataList = getDataList()
        if (dataList.isEmpty()) {
            log.warn("No data available for training.")
            return
        }

        if (dataList.any { it.allocationRate.isNaN() || it.allocationRate.isInfinite() }) {
            log.error("Invalid values (NaN or Infinity) detected in training data.")
            return
        }

        log.info("Training data size: ${dataList.size}")
        log.info("Sample training data: ${dataList.take(3)}")

        val features = dataList.map { extractor.extract(it) }.toTypedArray()
        val normalizedFeatures = normalize(features)
        val labels = dataList.map { it.label }.toIntArray()

        val df = DataFrame.of(
            DoubleVector.of("count", normalizedFeatures.map { it[0] }.toDoubleArray()),
            DoubleVector.of("time", normalizedFeatures.map { it[1] }.toDoubleArray()),
            DoubleVector.of("pause", normalizedFeatures.map { it[2] }.toDoubleArray()),
            DoubleVector.of("allocationRate", normalizedFeatures.map { it[3] }.toDoubleArray()),
            DoubleVector.of("liveDataSize", normalizedFeatures.map { it[4] }.toDoubleArray()),
            DoubleVector.of("gcStrategy", normalizedFeatures.map { it[5] }.toDoubleArray()),
            IntVector.of("label", labels)
        )

        val formula = Formula.lhs("label")
        val props = Properties().apply {
            // 하이퍼파라미터 설정
            setProperty("lambda", "1e-4")
            setProperty("tol", "1e-5")
            setProperty("maxIter", "500")
        }
        model = LogisticRegression.fit(formula, df, props)
        log.info("GcTrainer training completed.")

        val preds = normalizedFeatures.map { model!!.predict(it) }.toIntArray()
        val accuracy = preds.zip(labels).count { it.first == it.second }.toDouble() / labels.size
        log.info("📊 Training Accuracy: ${(accuracy * 100)}%")

        saveModel("train")
        saveModel("test")
    }

    private fun normalize(features: Array<DoubleArray>): Array<DoubleArray> {
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

    private fun saveModel(key: String) {
        val m = model ?: run {
            log.error("Model not trained. Cannot save [$key].")
            return
        }

        val file = File(modelDir, "gc_$key.model")

        ObjectOutputStream(file.outputStream().buffered()).use { oos ->
            oos.writeObject(m)
        }

        log.info("💾 Saved model [$key] → ${file.absolutePath}")
    }

    private fun getDataList(): List<GcTrainData> {
        // TODO - khope heesung이 만들어준 data get에서 가져와쓰는걸로 수정
        return listOf(
            GcTrainData(100, 400, 30, 1.2, 300_000, "G1", label = 1),
            GcTrainData(150, 700, 300, 3.8, 1_000_000, "G1", label = 0),
            GcTrainData(80, 250, 15, 0.8, 200_000, "Parallel", label = 1),
            GcTrainData(400, 1200, 700, 6.2, 2_000_000, "G1", label = 0),
            GcTrainData(90, 320, 20, 1.5, 350_000, "Serial", label = 1)
        )
    }
}
