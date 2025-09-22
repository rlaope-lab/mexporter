package lab.monilabexporterex.exporter.jvm.thread

import lab.monilabexporterex.exporter.data.JvmMonitoringData
import lab.monilabexporterex.exporter.jvm.ThreadExporter
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory
import java.lang.management.ThreadMXBean

@Component
class DefaultThreadExporter : ThreadExporter {
    override fun getThreadInfo(): JvmMonitoringData.Threads {
        val threadBean: ThreadMXBean = ManagementFactory.getThreadMXBean()

        val states =
            threadBean.allThreadIds
                .map { id ->
                    try {
                        threadBean.getThreadInfo(id)?.threadState
                    } catch (e: Exception) {
                        null
                    }
                }
                .groupingBy { it?.name ?: "" }
                .eachCount()

        return JvmMonitoringData.Threads(
            count = threadBean.threadCount,
            daemonCount = threadBean.daemonThreadCount,
            peakCount = threadBean.peakThreadCount,
            deadlockedCount = threadBean.findDeadlockedThreads()?.size ?: 0,
            cpuTime =
                if (threadBean.isThreadCpuTimeSupported) {
                    threadBean.allThreadIds.sumOf { id ->
                        try {
                            threadBean.getThreadCpuTime(id).takeIf { it > 0 } ?: 0
                        } catch (_: Exception) {
                            0
                        }
                    }
                } else {
                    0
                },
            states = states,
        )
    }
}
