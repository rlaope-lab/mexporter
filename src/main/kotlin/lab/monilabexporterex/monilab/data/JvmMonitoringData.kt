package lab.monilabexporterex.monilab.data

data class JvmMonitoringData(
    val hostname: String
) {
    data class Memory(
        val used: Long,
        val max: Long,
        val committed: Long,
        val eden: Long,
        val survivor: Long,
        val old: Long,
        val bufferPoolUsed: Long,
        val maxDirectMemorySize: Long
    )

    data class Gc(
        val count: Long,
        val time: Long,
        val pause: Long,
        val allocationRate: Double,
        val liveDataSize: Long,
        val gcStrategy: String
    )

    data class Threads(
        val count: Int,
        val daemonCount: Int,
        val peakCount: Int,
        val deadlockedCount: Int,
        val cpuTime: Long,
        val states: Map<String, Int>
    )

    data class Cpu(
        val processUsage: Double,
        val systemUsage: Double,
        val uptime: Long,
        val startTime: Long,
        val loadAverage: Double,
        val openFds: Long
    )

    data class Network(
        val bytesSent: Long,
        val bytesReceived: Long,
        val tcpConnections: Int,
        val tcpEstablished: Int,
        val openSockets: Int,
        val preferIPv4: Boolean
    )

    data class Classes(
        val loaded: Int,
        val unloaded: Long,
        val codeCacheUsed: Long,
        val codeCacheMax: Long,
        val compilationTime: Long,
        val reservedCodeCacheSize: Long
    )

    data class Application(
        val httpRequestsCount: Long,
        val httpLatency: Double,
        val dbConnectionsActive: Int,
        val dbConnectionsMax: Int,
        val queueTasksPending: Int,
        val customMetrics: Map<String, Any>
    )
}
