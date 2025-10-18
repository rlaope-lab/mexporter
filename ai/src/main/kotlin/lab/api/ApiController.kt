package lab.api

import lab.`ai-model`.gc.GcAnomalyDetector
import lab.`ai-model`.gc.GcLogisticRegressionTrainer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(
    private val gcLogisticRegressionTrainer: GcLogisticRegressionTrainer,
    private val gcAnomalyDetector: GcAnomalyDetector,
) {

    @GetMapping("/api/train")
    fun train(@RequestParam trainModelName: String?) {
        when(trainModelName) {
            "gc_logistic_regression" -> gcLogisticRegressionTrainer.train()
            "gc_anomaly_detector" -> gcAnomalyDetector.train()
            else -> throw IllegalArgumentException("Unknown model name: $trainModelName")
        }
    }
}
