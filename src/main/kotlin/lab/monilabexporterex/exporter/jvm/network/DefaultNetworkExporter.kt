package lab.monilabexporterex.exporter.jvm.network

import lab.monilabexporterex.cmd.CmdFactory
import lab.monilabexporterex.cmd.CmdInterface
import lab.monilabexporterex.exporter.data.JvmMonitoringData
import lab.monilabexporterex.exporter.jvm.NetworkExporter
import org.springframework.stereotype.Component
import oshi.SystemInfo

@Component
class DefaultNetworkExporter : NetworkExporter {

    private val systemInfo = SystemInfo()
    private val cmdUtil: CmdInterface = CmdFactory.getCmdUtil()

    override fun getNetworkInfo(): JvmMonitoringData.Network {
        val hw = systemInfo.hardware
        val networkIFs = hw.networkIFs

        var bytesSent = 0L
        var bytesReceived = 0L

        networkIFs.forEach {
            it.updateAttributes()
            bytesSent += it.bytesSent
            bytesReceived += it.bytesRecv
        }

        val tcpConnections = cmdUtil.getTcpConnections()
        val tcpEstablished = cmdUtil.getTcpEstablished()
        val openSockets = cmdUtil.getOpenSockets()
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
}
