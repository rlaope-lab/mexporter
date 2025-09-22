package lab.monilabexporterex.api

import lab.monilabexporterex.api.dto.*
import lab.monilabexporterex.exporter.JvmExporter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExporterController(
    private val jvmExporter: JvmExporter
) {
    @GetMapping("/memory")
    fun getMemoryInfo(): ResponseEntity<MemoryResponse> {
        val data = jvmExporter.getMemoryInfo()
        return ResponseEntity.ok(
            MemoryResponse(
                hostname = jvmExporter.getHostname(),
                used = data.used,
                max = data.max,
                committed = data.committed,
                eden = data.eden,
                survivor = data.survivor,
                old = data.old,
                bufferPoolUsed = data.bufferPoolUsed,
                maxDirectMemorySize = data.maxDirectMemorySize
            )
        )
    }

    @GetMapping("/gc")
    fun getGcInfo(): ResponseEntity<GcResponse> {
        val data = jvmExporter.getGcInfo()
        return ResponseEntity.ok(
            GcResponse(
                hostname = jvmExporter.getHostname(),
                count = data.count,
                time = data.time,
                pause = data.pause,
                allocationRate = data.allocationRate,
                liveDataSize = data.liveDataSize,
                gcStrategy = data.gcStrategy
            )
        )
    }

    @GetMapping("/threads")
    fun getThreadsInfo(): ResponseEntity<ThreadsResponse> {
        val data = jvmExporter.getThreadInfo()
        return ResponseEntity.ok(
            ThreadsResponse(
                hostname = jvmExporter.getHostname(),
                count = data.count,
                daemonCount = data.daemonCount,
                peakCount = data.peakCount,
                deadlockedCount = data.deadlockedCount,
                cpuTime = data.cpuTime,
                states = data.states
            )
        )
    }

    @GetMapping("/cpu")
    fun getCpuInfo(): ResponseEntity<CpuResponse> {
        val data = jvmExporter.getCpuInfo()
        return ResponseEntity.ok(
            CpuResponse(
                hostname = jvmExporter.getHostname(),
                processUsage = data.processUsage,
                systemUsage = data.systemUsage,
                uptime = data.uptime,
                startTime = data.startTime,
                loadAverage = data.loadAverage,
                openFds = data.openFds
            )
        )
    }

    @GetMapping("/network")
    fun getNetworkInfo(): ResponseEntity<NetworkResponse> {
        val data = jvmExporter.getNetworkInfo()
        return ResponseEntity.ok(
            NetworkResponse(
                hostname = jvmExporter.getHostname(),
                bytesSent = data.bytesSent,
                bytesReceived = data.bytesReceived,
                tcpConnections = data.tcpConnections,
                tcpEstablished = data.tcpEstablished,
                openSockets = data.openSockets,
                preferIPv4 = data.preferIPv4
            )
        )
    }

    @GetMapping("/classloading")
    fun getClassLoadingInfo(): ResponseEntity<ClassLoadingInfoResponse> {
        val data = jvmExporter.getClassLoadingInfo()
        return ResponseEntity.ok(
            ClassLoadingInfoResponse(
                hostname = jvmExporter.getHostname(),
                loaded = data.loaded,
                unloaded = data.unloaded,
                codeCacheUsed = data.codeCacheUsed,
                codeCacheMax = data.codeCacheMax,
                compilationTime = data.compilationTime,
                reservedCodeCacheSize = data.reservedCodeCacheSize
            )
        )
    }

    @GetMapping("/application")
    fun getApplicationInfo(): ResponseEntity<ApplicationResponse> {
        val data = jvmExporter.getApplicationInfo()
        return ResponseEntity.ok(
            ApplicationResponse(
                hostname = jvmExporter.getHostname(),
                httpRequestsCount = data.httpRequestsCount,
                httpLatency = data.httpLatency,
                dbConnectionsActive = data.dbConnectionsActive,
                dbConnectionsMax = data.dbConnectionsMax,
                queueTasksPending = data.queueTasksPending,
                customMetrics = data.customMetrics
            )
        )
    }
}
