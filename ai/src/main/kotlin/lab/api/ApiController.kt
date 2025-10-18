package lab.api

import lab.`ai-model`.gc.GcTrainer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(
    private val gcTrainer: GcTrainer
) {

    @GetMapping("/api/train")
    fun train() {
        gcTrainer.train()
    }
}
