package lab.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(

) {

    @GetMapping("/api/train")
    fun train() {

    }
}
