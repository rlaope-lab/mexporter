package lab.monilabexporterex.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class SchedulerConfig {

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = 5
        scheduler.setThreadNamePrefix("metric-scheduler-")
        return scheduler
    }

}
