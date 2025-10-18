package lab.`ai-model`.gc

import lab.`ai-model`.NormalizationUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import smile.clustering.KMeans
import smile.math.distance.EuclideanDistance
import java.io.File
import java.io.ObjectOutputStream

@Component
class GcAnomalyDetector {

    private val extractor: GcFeatureExtractor by lazy { GcFeatureExtractor }
    private val normalizationUtil: NormalizationUtil by lazy { NormalizationUtil }

    private val projectRootDir: String = System.getProperty("user.dir")
    private val modelDir = File("$projectRootDir/ai-models/gc-anomaly").apply { mkdirs() }

    private lateinit var model: KMeans

    private val log = LoggerFactory.getLogger(GcAnomalyDetector::class.java)

    fun train(k: Int = 3) {
        log.info("Start GcAnomalyDetector(KMeans) training...")

        val dataList = getDataList()
        if (dataList.isEmpty()) {
            log.warn("No data available for training.")
            return
        }

        val features = dataList.map { extractor.extract(it) }.toTypedArray()
        val normalized = normalizationUtil.normalize(features)

        // KMeans 학습
        model = KMeans.fit(normalized, k)
        log.info("✅ KMeans training completed with $k clusters.")
        saveModel()
    }

    fun predict(data: GcTrainData): Double {
        val dist = EuclideanDistance()
        val features = extractor.extract(data)
        val cluster = model.predict(features)
        val centroid = model.centroids[cluster]
        val distance = dist.d(features, centroid)
        log.info("🔎 Distance to cluster center: %.4f".format(distance))
        return distance
    }

    private fun saveModel() {
        val file = File(modelDir, "gc_anomaly_kmeans.model")
        ObjectOutputStream(file.outputStream().buffered()).use { oos ->
            oos.writeObject(model)
        }
        log.info("💾 Saved KMeans anomaly model → ${file.absolutePath}")
    }

    private fun getDataList(): List<GcTrainData> {
        // TODO - 실제 exporter 데이터 연결
        return listOf(
            GcTrainData(120, 500, 40, 1.2, 400_000, "G1", label = 1),
            GcTrainData(200, 800, 350, 3.8, 1_200_000, "G1", label = 1),
            GcTrainData(85, 260, 12, 0.9, 250_000, "Parallel", label = 1),
            GcTrainData(600, 1500, 900, 6.8, 2_200_000, "G1", label = 1),
            GcTrainData(95, 340, 18, 1.4, 370_000, "Serial", label = 1)
        )
    }
}
