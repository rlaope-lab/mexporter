package lab.`ai-model`.gc

import lab.`ai-model`.NormalizationUtil
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
class GcLogisticRegressionTrainer {
    private val extractor: GcFeatureExtractor by lazy { GcFeatureExtractor }
    private val normalizationUtil: NormalizationUtil by lazy { NormalizationUtil }

    private val projectRootDir: String = System.getProperty("user.dir")
    private val modelDir = File("$projectRootDir/ai-models/gc-model").apply { mkdirs() }

    private lateinit var model: LogisticRegression

    private val log = LoggerFactory.getLogger(GcLogisticRegressionTrainer::class.java)

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
        val normalizedFeatures = normalizationUtil.normalize(features)
        val labels = dataList.map { it.label }.toIntArray()

        val df =
            DataFrame.of(
                DoubleVector.of("count", normalizedFeatures.map { it[0] }.toDoubleArray()),
                DoubleVector.of("time", normalizedFeatures.map { it[1] }.toDoubleArray()),
                DoubleVector.of("pause", normalizedFeatures.map { it[2] }.toDoubleArray()),
                DoubleVector.of("allocationRate", normalizedFeatures.map { it[3] }.toDoubleArray()),
                DoubleVector.of("liveDataSize", normalizedFeatures.map { it[4] }.toDoubleArray()),
                DoubleVector.of("gcStrategy", normalizedFeatures.map { it[5] }.toDoubleArray()),
                IntVector.of("label", labels),
            )

        val formula = Formula.lhs("label")
        val props =
            Properties().apply {
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

    private fun saveModel(key: String) {
        val m =
            model ?: run {
                log.error("Model not trained. Cannot save [$key].")
                return
            }

        val file = File(modelDir, "gc_$key.model")

        ObjectOutputStream(file.outputStream().buffered()).use { oos ->
            oos.writeObject(m)
        }

        log.info("💾 Saved model [$key] → ${file.absolutePath}")
    }

    private fun getDataList(isTestSet: Boolean = true): List<GcTrainData> {
        return if (isTestSet) {
            listOf(
                GcTrainData(120, 500, 40, 1.2, 400_000, "G1", label = 1),
                GcTrainData(200, 800, 350, 3.8, 1_200_000, "G1", label = 1),
                GcTrainData(85, 260, 12, 0.9, 250_000, "Parallel", label = 1),
                GcTrainData(600, 1500, 900, 6.8, 2_200_000, "G1", label = 1),
                GcTrainData(95, 340, 18, 1.4, 370_000, "Serial", label = 1),
            )
        } else {
            // TODO heesung feature
            listOf()
        }
    }
}
