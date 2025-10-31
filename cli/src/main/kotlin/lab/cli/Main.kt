package lab.cli

import lab.monilabexporterex.config.ExporterConfig
import lab.monilabexporterex.exporter.JvmExporter
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
    val context =
        SpringApplicationBuilder(ExporterConfig::class.java)
            .web(WebApplicationType.NONE)
            .bannerMode(Banner.Mode.OFF)
            .logStartupInfo(false)
            .run(*args)

    val exporter = context.getBean(JvmExporter::class.java)

    val mode = args[0].uppercase()
    when (mode) {
        "SUMMARY" -> printSummary(exporter)
        "GC" -> loop { printGc(exporter) }
        "MEMORY" -> loop { printMemory(exporter) }
        "CPU" -> loop { printCpu(exporter) }
        "THREADS" -> loop { printThreads(exporter) }
        "CLASSLOADING" -> loop { printClassLoading(exporter) }
        "NETWORK" -> loop { printNetwork(exporter) }
        "APPLICATION" -> loop { printApplication(exporter) }
        else -> println("Unknown mode: $mode")
    }
}

private fun loop(action: () -> Unit) {
    while (true) {
        print("\u001b[H\u001b[2J")
        System.out.flush()
        action()
        Thread.sleep(1000) // 1초마다 갱신
    }
}

private fun printSummary(exporter: JvmExporter) =
    loop {
        val mem = exporter.getMemoryInfo()
        val gc = exporter.getGcInfo()
        val cpu = exporter.getCpuInfo()
        val threads = exporter.getThreadInfo()

        println(
            """
            ==== JVM Summary ====
            Host: ${exporter.getHostname()}
            Memory Used: ${mem.used / 1024 / 1024} MB / ${mem.max / 1024 / 1024} MB
            GC Count: ${gc.count}, Time: ${gc.time} ms
            CPU Usage: ${"%.2f".format(cpu.processUsage * 100)} %
            Threads: ${threads.count} (Daemon: ${threads.daemonCount})
            =====================
            """.trimIndent(),
        )
    }

private fun printGc(exporter: JvmExporter) {
    val gc = exporter.getGcInfo()
    println("GC -> count=${gc.count}, time=${gc.time}, strategy=${gc.gcStrategy}")
}

private fun printMemory(exporter: JvmExporter) {
    val mem = exporter.getMemoryInfo()
    println("Memory -> used=${mem.used}, max=${mem.max}, old=${mem.old}")
}

private fun printCpu(exporter: JvmExporter) {
    val cpu = exporter.getCpuInfo()
    println("CPU -> process=${cpu.processUsage}, system=${cpu.systemUsage}, uptime=${cpu.uptime}")
}

private fun printThreads(exporter: JvmExporter) {
    val th = exporter.getThreadInfo()
    println("Threads -> count=${th.count}, daemon=${th.daemonCount}, peak=${th.peakCount}, deadlocked=${th.deadlockedCount}")
}

private fun printClassLoading(exporter: JvmExporter) {
    val cl = exporter.getClassLoadingInfo()
    println("ClassLoading -> loaded=${cl.loaded}, unloaded=${cl.unloaded}, codeCacheUsed=${cl.codeCacheUsed}")
}

private fun printNetwork(exporter: JvmExporter) {
    val net = exporter.getNetworkInfo()
    println("Network -> sent=${net.bytesSent}, received=${net.bytesReceived}, connections=${net.tcpConnections}")
}

private fun printApplication(exporter: JvmExporter) {
    val app = exporter.getApplicationInfo()
    println("Application -> httpRequests=${app.httpRequestsCount}, latency=${app.httpLatency}, dbActive=${app.dbConnectionsActive}")
}
