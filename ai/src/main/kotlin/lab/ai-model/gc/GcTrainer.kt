package lab.`ai-model`.gc

import lab.monilabexporterex.exporter.data.JvmMonitoringData
import org.slf4j.LoggerFactory
import smile.classification.LogisticRegression
import smile.data.DataFrame
import smile.data.formula.Formula
import smile.data.vector.DoubleVector
import smile.data.vector.IntVector
import java.io.File
import java.io.ObjectOutputStream

class GcTrainer {
    private val extractor: GcFeatureExtractor by lazy { GcFeatureExtractor }
    private var model: LogisticRegression? = null
    private val log = LoggerFactory.getLogger(GcTrainer::class.java)

    private val projectRootDir: String =
        File(GcTrainer::class.java.protectionDomain.codeSource.location.toURI())
            .parentFile.parentFile.parentFile.absolutePath
    private val modelDir = File("$projectRootDir/ai-models").apply { mkdirs() }

    fun train() {
        log.info("Start GcTrainer training...")
        val dataList = getDataList()
        if (dataList.isEmpty()) {
            log.warn("No data available for training.")
            return
        }

        log.info("Training data size: ${dataList.size}")
        log.info("Sample training data: ${dataList.take(3)}")

        val features = dataList.map { extractor.extract(it) }.toTypedArray()
        val labels = dataList.map { it.label }.toIntArray()
        val df = DataFrame.of(
            DoubleVector.of("count", features.map { it[0] }.toDoubleArray()),
            DoubleVector.of("time", features.map { it[1] }.toDoubleArray()),
            DoubleVector.of("pause", features.map { it[2] }.toDoubleArray()),
            DoubleVector.of("allocationRate", features.map { it[3] }.toDoubleArray()),
            DoubleVector.of("liveDataSize", features.map { it[4] }.toDoubleArray()),
            DoubleVector.of("gcStrategy", features.map { it[5] }.toDoubleArray()),
            IntVector.of("label", labels)
        )

        val formula = Formula.lhs("label")
        model = LogisticRegression.fit(formula, df)
        log.info("GcTrainer training completed.")

        saveModel("train")
        saveModel("test")
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
