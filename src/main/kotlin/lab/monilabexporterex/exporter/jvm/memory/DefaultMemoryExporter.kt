package lab.monilabexporterex.exporter.jvm.memory

import lab.monilabexporterex.exporter.data.JvmMonitoringData
import lab.monilabexporterex.exporter.jvm.MemoryExporter
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory

@Component
class DefaultMemoryExporter : MemoryExporter {
    override fun getMemoryInfo(): JvmMonitoringData.Memory {
        val memoryBean = ManagementFactory.getMemoryMXBean()
        val heap = memoryBean.heapMemoryUsage
        val nonHeap = memoryBean.nonHeapMemoryUsage

        val pools: Map<String, Long> = ManagementFactory.getMemoryPoolMXBeans()
            .associate { it.name to it.usage.used }

        return JvmMonitoringData.Memory(
            used = heap.used + nonHeap.used,
            max = (heap.max + nonHeap.max),
            committed = heap.committed + nonHeap.committed,
            eden = pools["PS Eden Space"] ?: 0,
            survivor = pools["PS Survivor Space"] ?: 0,
            old = pools["PS Old Gen"] ?: 0,
            bufferPoolUsed = 0,
            maxDirectMemorySize = Runtime.getRuntime().maxMemory()
        )
    }
}
