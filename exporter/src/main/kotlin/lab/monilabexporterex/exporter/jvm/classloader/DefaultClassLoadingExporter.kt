package lab.monilabexporterex.exporter.jvm.classloader

import lab.monilabexporterex.exporter.data.JvmMonitoringData
import lab.monilabexporterex.exporter.jvm.ClassLoadingExporter
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory

@Component
class DefaultClassLoadingExporter : ClassLoadingExporter {
    override fun getClassLoadingInfo(): JvmMonitoringData.ClassLoadingInfo {
        val classLoadingMXBean = ManagementFactory.getClassLoadingMXBean()
        val compilationMXBean = ManagementFactory.getCompilationMXBean()

        // TODO Code Cache 메모리풀 찾기인데 더 좋은방법 없을까 고민점
        val codeCachePool = ManagementFactory.getMemoryPoolMXBeans()
            .find { it.name == "Code Cache" }

        val codeCacheUsed = codeCachePool?.usage?.used ?: 0L
        val codeCacheMax = codeCachePool?.usage?.max ?: -1L
        val reservedCodeCacheSize = codeCachePool?.usage?.committed ?: 0L

        return JvmMonitoringData.ClassLoadingInfo(
            loaded = classLoadingMXBean.loadedClassCount,
            unloaded = classLoadingMXBean.unloadedClassCount,
            codeCacheUsed = codeCacheUsed,
            codeCacheMax = codeCacheMax,
            compilationTime = compilationMXBean.totalCompilationTime,
            reservedCodeCacheSize = reservedCodeCacheSize
        )
    }
}

