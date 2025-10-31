package lab.monilabexporterex.mapper

import lab.monilabexporterex.exporter.data.JvmMonitoringData
import lab.monilabexporterex.model.*
import java.time.LocalDateTime

object JvmMonitoringMapper {
    fun toEntity(
        data: JvmMonitoringData.Memory,
        label: Label,
    ): MemoryEntity =
        MemoryEntity(
            label = label,
            used = data.used,
            max = data.max,
            committed = data.committed,
            eden = data.eden,
            survivor = data.survivor,
            old = data.old,
            bufferPoolUsed = data.bufferPoolUsed,
            maxDirectMemorySize = data.maxDirectMemorySize,
            registeredDateTime = LocalDateTime.now(),
        )

    fun toData(entity: MemoryEntity): JvmMonitoringData.Memory =
        JvmMonitoringData.Memory(
            used = entity.used,
            max = entity.max,
            committed = entity.committed,
            eden = entity.eden,
            survivor = entity.survivor,
            old = entity.old,
            bufferPoolUsed = entity.bufferPoolUsed,
            maxDirectMemorySize = entity.maxDirectMemorySize,
        )

    fun toEntity(
        data: JvmMonitoringData.Gc,
        label: Label,
    ): GcEntity =
        GcEntity(
            label = label,
            count = data.count,
            time = data.time,
            pause = data.pause,
            allocationRate = data.allocationRate,
            liveDataSize = data.liveDataSize,
            gcStrategy = data.gcStrategy,
            registeredDateTime = LocalDateTime.now(),
        )

    fun toData(entity: GcEntity): JvmMonitoringData.Gc =
        JvmMonitoringData.Gc(
            count = entity.count,
            time = entity.time,
            pause = entity.pause,
            allocationRate = entity.allocationRate,
            liveDataSize = entity.liveDataSize,
            gcStrategy = entity.gcStrategy,
        )

    fun toEntity(
        data: JvmMonitoringData.Threads,
        label: Label,
    ): ThreadsEntity =
        ThreadsEntity(
            label = label,
            count = data.count,
            daemonCount = data.daemonCount,
            peakCount = data.peakCount,
            deadlockedCount = data.deadlockedCount,
            cpuTime = data.cpuTime,
            states = data.states.toString(),
            registeredDateTime = LocalDateTime.now(),
        )

    fun toData(entity: ThreadsEntity): JvmMonitoringData.Threads =
        JvmMonitoringData.Threads(
            count = entity.count,
            daemonCount = entity.daemonCount,
            peakCount = entity.peakCount,
            deadlockedCount = entity.deadlockedCount,
            cpuTime = entity.cpuTime,
            states = parseStates(entity.states),
        )

    private fun parseStates(json: String): Map<String, Int> {
        return json
            .removePrefix("{").removeSuffix("}")
            .split(",")
            .mapNotNull {
                val (k, v) = it.split("=")
                k.trim() to v.trim().toInt()
            }.toMap()
    }

    fun toEntity(
        data: JvmMonitoringData.Cpu,
        label: Label,
    ): CpuEntity =
        CpuEntity(
            label = label,
            processUsage = data.processUsage,
            systemUsage = data.systemUsage,
            uptime = data.uptime,
            startTime = data.startTime,
            loadAverage = data.loadAverage,
            openFds = data.openFds,
            registeredDateTime = LocalDateTime.now(),
        )

    fun toData(entity: CpuEntity): JvmMonitoringData.Cpu =
        JvmMonitoringData.Cpu(
            processUsage = entity.processUsage,
            systemUsage = entity.systemUsage,
            uptime = entity.uptime,
            startTime = entity.startTime,
            loadAverage = entity.loadAverage,
            openFds = entity.openFds,
        )

    fun toEntity(
        data: JvmMonitoringData.Network,
        label: Label,
    ): NetworkEntity =
        NetworkEntity(
            label = label,
            bytesSent = data.bytesSent,
            bytesReceived = data.bytesReceived,
            tcpConnections = data.tcpConnections,
            tcpEstablished = data.tcpEstablished,
            openSockets = data.openSockets,
            preferIPv4 = data.preferIPv4,
            registeredDateTime = LocalDateTime.now(),
        )

    fun toData(entity: NetworkEntity): JvmMonitoringData.Network =
        JvmMonitoringData.Network(
            bytesSent = entity.bytesSent,
            bytesReceived = entity.bytesReceived,
            tcpConnections = entity.tcpConnections,
            tcpEstablished = entity.tcpEstablished,
            openSockets = entity.openSockets,
            preferIPv4 = entity.preferIPv4,
        )

    fun toEntity(
        data: JvmMonitoringData.ClassLoadingInfo,
        label: Label,
    ): ClassLoadingInfoEntity =
        ClassLoadingInfoEntity(
            label = label,
            loaded = data.loaded,
            unloaded = data.unloaded,
            codeCacheUsed = data.codeCacheUsed,
            codeCacheMax = data.codeCacheMax,
            compilationTime = data.compilationTime,
            reservedCodeCacheSize = data.reservedCodeCacheSize,
            registeredDateTime = LocalDateTime.now(),
        )

    fun toData(entity: ClassLoadingInfoEntity): JvmMonitoringData.ClassLoadingInfo =
        JvmMonitoringData.ClassLoadingInfo(
            loaded = entity.loaded,
            unloaded = entity.unloaded,
            codeCacheUsed = entity.codeCacheUsed,
            codeCacheMax = entity.codeCacheMax,
            compilationTime = entity.compilationTime,
            reservedCodeCacheSize = entity.reservedCodeCacheSize,
        )

    fun toEntity(
        data: JvmMonitoringData.Application,
        label: Label,
    ): ApplicationEntity =
        ApplicationEntity(
            label = label,
            httpRequestsCount = data.httpRequestsCount,
            httpLatency = data.httpLatency,
            dbConnectionsActive = data.dbConnectionsActive,
            dbConnectionsMax = data.dbConnectionsMax,
            queueTasksPending = data.queueTasksPending,
            customMetrics = data.customMetrics.toString(),
            registeredDateTime = LocalDateTime.now(),
        )

    fun toData(entity: ApplicationEntity): JvmMonitoringData.Application =
        JvmMonitoringData.Application(
            httpRequestsCount = entity.httpRequestsCount,
            httpLatency = entity.httpLatency,
            dbConnectionsActive = entity.dbConnectionsActive,
            dbConnectionsMax = entity.dbConnectionsMax,
            queueTasksPending = entity.queueTasksPending,
            customMetrics = parseCustomMetrics(entity.customMetrics),
        )

    private fun parseCustomMetrics(json: String): Map<String, Any> {
        return json
            .removePrefix("{").removeSuffix("}")
            .split(",")
            .mapNotNull {
                val parts = it.split("=")
                if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
            }.toMap()
    }
}
