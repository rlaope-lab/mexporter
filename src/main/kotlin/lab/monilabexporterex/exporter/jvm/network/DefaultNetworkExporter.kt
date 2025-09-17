package lab.monilabexporterex.exporter.jvm.network

import lab.monilabexporterex.exporter.data.JvmMonitoringData
import org.springframework.stereotype.Component
import oshi.SystemInfo
import oshi.hardware.NetworkIF
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class DefaultNetworkExporter{

    private val systemInfo = SystemInfo()

    fun getNetworkInfo(): JvmMonitoringData.Network {
        val hw = systemInfo.hardware
        val networkIFs: List<NetworkIF> = hw.networkIFs

        var bytesSent = 0L
        var bytesReceived = 0L

        networkIFs.forEach { net ->
            net.updateAttributes()
            bytesSent += net.bytesSent
            bytesReceived += net.bytesRecv
        }

        val (tcpConnections, tcpEstablished, openSockets) = getTcpAndSocketStats()

        val preferIPv4 = System.getProperty("java.net.preferIPv4Stack") == "true"

        return JvmMonitoringData.Network(
            bytesSent = bytesSent,
            bytesReceived = bytesReceived,
            tcpConnections = tcpConnections,
            tcpEstablished = tcpEstablished,
            openSockets = openSockets,
            preferIPv4 = preferIPv4
        )
    }

    private fun getTcpAndSocketStats(): Triple<Int, Int, Int> {
        var tcpConnections = 0
        var tcpEstablished = 0
        var openSockets = 0

        try {
            val os = System.getProperty("os.name").lowercase()

            if (os.contains("linux") || os.contains("mac")) {
                tcpConnections = runCommand("sh", "-c", "netstat -nat | wc -l")
                tcpEstablished = runCommand("sh", "-c", "netstat -nat | grep ESTABLISHED | wc -l")
                openSockets = runCommand("sh", "-c", "lsof -i | wc -l")
            } else if (os.contains("win")) {
                tcpConnections = runCommand("cmd", "/c", "netstat -an | find /c \"TCP\"")
                tcpEstablished = runCommand("cmd", "/c", "netstat -an | find /c \"ESTABLISHED\"")
                openSockets = runCommand("cmd", "/c", "netstat -an | find /c \"\"")
            }

        } catch (_: Exception) { }

        return Triple(tcpConnections, tcpEstablished, openSockets)
    }

    private fun runCommand(vararg command: String): Int {
        return try {
            val process = ProcessBuilder(*command)
                .redirectErrorStream(true)
                .start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText().trim()
            process.waitFor()
            output.toIntOrNull() ?: 0
        } catch (ex: Exception) {
            0
        }
    }
}
