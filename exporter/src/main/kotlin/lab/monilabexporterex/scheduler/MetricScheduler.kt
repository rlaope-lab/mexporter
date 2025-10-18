package lab.monilabexporterex.scheduler

import lab.monilabexporterex.exporter.JvmExporter
import lab.monilabexporterex.mapper.JvmMonitoringMapper
import lab.monilabexporterex.model.Label.*
import lab.monilabexporterex.repository.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class JvmMonitoringScheduler(
    private val jvmExporter: JvmExporter,
    private val memoryRepository: MemoryRepository,
    private val gcRepository: GcRepository,
    private val threadsRepository: ThreadsRepository,
    private val cpuRepository: CpuRepository,
    private val networkRepository: NetworkRepository,
    private val classLoadingRepository: ClassLoadingInfoRepository,
    private val applicationRepository: ApplicationRepository
) {

    @Scheduled(fixedRate = 1000)
    fun collectAndSaveJvmMetrics() {
        val memoryEntity = JvmMonitoringMapper.toEntity(jvmExporter.getMemoryInfo(), TEST)
        memoryRepository.save(memoryEntity)

        val gcEntity = JvmMonitoringMapper.toEntity(jvmExporter.getGcInfo(), TEST)
        gcRepository.save(gcEntity)

        val threadsEntity = JvmMonitoringMapper.toEntity(jvmExporter.getThreadInfo(), TEST)
        threadsRepository.save(threadsEntity)

        val cpuEntity = JvmMonitoringMapper.toEntity(jvmExporter.getCpuInfo(), TEST)
        cpuRepository.save(cpuEntity)

        val networkEntity = JvmMonitoringMapper.toEntity(jvmExporter.getNetworkInfo(), TEST)
        networkRepository.save(networkEntity)

        val classLoadingEntity = JvmMonitoringMapper.toEntity(jvmExporter.getClassLoadingInfo(), TEST)
        classLoadingRepository.save(classLoadingEntity)

        val applicationEntity = JvmMonitoringMapper.toEntity(jvmExporter.getApplicationInfo(), TEST)
        applicationRepository.save(applicationEntity)
    }

}
