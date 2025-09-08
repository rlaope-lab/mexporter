package lab.monilabexporterex.exporter.jvm.gc

import com.sun.management.GarbageCollectionNotificationInfo
import lab.monilabexporterex.exporter.data.JvmMonitoringData
import lab.monilabexporterex.exporter.jvm.GcExporter
import java.lang.management.GarbageCollectorMXBean
import java.lang.management.ManagementFactory
import javax.management.NotificationEmitter
import javax.management.NotificationListener
import javax.management.openmbean.CompositeData

class DefaultGcExporter : GcExporter {

    /**
     * 호출 시점에만 MXBean 읽기 → 불가능
     * pause: 누적값만 가능, 이벤트 없이는 못 구함
     * allocationRate: 샘플링 필요
     * liveDataSize: 이벤트 기반 필요
     * -> MXBean → 누적 통계 (count, total time, heap size 등)
     * -> + NotificationListener (GC 이벤트) → pause time, live data size 샘플링 로직 → allocationRate
     */

    @Volatile private var lastPauseTime: Long = 0
    @Volatile private var lastLiveDataSize: Long = 0
    @Volatile private var lastHeapUsed: Long = 0
    @Volatile private var lastTimestamp: Long = System.currentTimeMillis()

    init {
        // GC Notification Listener 등록
        ManagementFactory.getGarbageCollectorMXBeans().forEach { gc ->
            if (gc is NotificationEmitter) {
                gc.addNotificationListener(
                    NotificationListener { notification, _ ->
                        if (notification.type == GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION) {
                            val userData = notification.userData as CompositeData
                            val info = GarbageCollectionNotificationInfo.from(userData)

                            // pause time 업데이트
                            lastPauseTime += info.gcInfo.duration

                            // live data size (GC 후 Old 영역 크기)
                            info.gcInfo.memoryUsageAfterGc.forEach { (name, usage) ->
                                if (name.contains("Old") || name.contains("Tenured")) {
                                    lastLiveDataSize = usage.used
                                }
                            }
                        }
                    },
                    null, null
                )
            }
        }
    }

    override fun getGcInfo(): JvmMonitoringData.Gc {
        val gcBeans: List<GarbageCollectorMXBean> = ManagementFactory.getGarbageCollectorMXBeans()
        val totalCount = gcBeans.sumOf { it.collectionCount.takeIf { c -> c >= 0 } ?: 0 }
        val totalTime = gcBeans.sumOf { it.collectionTime.takeIf { t -> t >= 0 } ?: 0 }

        // 현재 힙 사용량
        val heapUsage = ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
        val now = System.currentTimeMillis()
        val deltaTimeSec = (now - lastTimestamp) / 1000.0

        val allocationRate = if (deltaTimeSec > 0) {
            (heapUsage - lastHeapUsed).coerceAtLeast(0) / deltaTimeSec
        } else 0.0

        // 상태 갱신
        lastHeapUsed = heapUsage
        lastTimestamp = now

        val gcNames = gcBeans.map { it.name }.toSet()
        val gcStrategy = when {
            gcNames.any { it.contains("G1") } -> "G1"
            gcNames.any { it.contains("ZGC") } -> "ZGC"
            gcNames.any { it.contains("Shenandoah") } -> "Shenandoah"
            gcNames.any { it.contains("Parallel") } -> "Parallel"
            gcNames.any { it.contains("CMS") } -> "CMS"
            else -> gcNames.joinToString(", ")
        }

        return JvmMonitoringData.Gc(
            count = totalCount,
            time = totalTime,
            pause = lastPauseTime,
            allocationRate = allocationRate,
            liveDataSize = lastLiveDataSize,
            gcStrategy = gcStrategy
        )
    }
}
