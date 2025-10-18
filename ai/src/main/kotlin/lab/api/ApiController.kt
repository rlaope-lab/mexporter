package lab.api

import lab.`ai-model`.gc.GcLogisticRegressionTrainer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(
    private val gcLogisticRegressionTrainer: GcLogisticRegressionTrainer
) {

    @GetMapping("/api/train")
    fun train() {
        gcLogisticRegressionTrainer.train()
    }
}
