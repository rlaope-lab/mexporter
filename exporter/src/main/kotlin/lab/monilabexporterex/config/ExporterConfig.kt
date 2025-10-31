package lab.monilabexporterex.config

import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import lab.monilabexporterex.exporter.JvmExporter
import lab.monilabexporterex.exporter.jvm.*
import lab.monilabexporterex.exporter.jvm.application.DefaultApplicationExporter
import lab.monilabexporterex.exporter.jvm.classloader.DefaultClassLoadingExporter
import lab.monilabexporterex.exporter.jvm.cpu.DefaultCpuExporter
import lab.monilabexporterex.exporter.jvm.gc.DefaultGcExporter
import lab.monilabexporterex.exporter.jvm.memory.DefaultMemoryExporter
import lab.monilabexporterex.exporter.jvm.network.DefaultNetworkExporter
import lab.monilabexporterex.exporter.jvm.thread.DefaultThreadExporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.BlockingQueue

@Configuration
class ExporterConfig {
    @Bean
    fun meterRegistry(): MeterRegistry = SimpleMeterRegistry()

    @Bean
    fun applicationExporter(
        meterRegistry: MeterRegistry,
        dataSource: HikariDataSource? = null,
        taskQueue: BlockingQueue<*>? = null,
    ): ApplicationExporter = DefaultApplicationExporter(meterRegistry, dataSource, taskQueue)

    @Bean
    fun classLoadingExporter(): ClassLoadingExporter = DefaultClassLoadingExporter()

    @Bean
    fun cpuExporter(): CpuExporter = DefaultCpuExporter()

    @Bean
    fun gcExporter(): GcExporter = DefaultGcExporter()

    @Bean
    fun memoryExporter(): MemoryExporter = DefaultMemoryExporter()

    @Bean
    fun networkExporter(): NetworkExporter = DefaultNetworkExporter()

    @Bean
    fun threadExporter(): ThreadExporter = DefaultThreadExporter()

    @Bean
    fun jvmExporter(
        memoryExporter: MemoryExporter,
        gcExporter: GcExporter,
        threadExporter: ThreadExporter,
        cpuExporter: CpuExporter,
        classLoadingExporter: ClassLoadingExporter,
        networkExporter: NetworkExporter,
        applicationExporter: ApplicationExporter,
    ): JvmExporter =
        JvmExporterImpl(
            memoryExporter,
            gcExporter,
            threadExporter,
            cpuExporter,
            classLoadingExporter,
            networkExporter,
            applicationExporter,
        )
}
