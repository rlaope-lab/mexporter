package lab.monilabexporterex.exporter.jvm

import lab.monilabexporterex.exporter.JvmExporter
import org.springframework.stereotype.Component
import java.net.InetAddress

@Component
class JvmExporterImpl(
    private val memoryExporter: MemoryExporter,
    private val gcExporter: GcExporter,
    private val threadExporter: ThreadExporter,
    private val cpuExporter: CpuExporter,
    private val classLoadingExporter: ClassLoadingExporter,
    private val networkExporter: NetworkExporter,
    private val applicationExporter: ApplicationExporter,
) : JvmExporter {
    override fun getHostname(): String = InetAddress.getLocalHost().hostName

    override fun getMemoryInfo() = memoryExporter.getMemoryInfo()

    override fun getGcInfo() = gcExporter.getGcInfo()

    override fun getThreadInfo() = threadExporter.getThreadInfo()

    override fun getCpuInfo() = cpuExporter.getCpuInfo()

    override fun getClassLoadingInfo() = classLoadingExporter.getClassLoadingInfo()

    override fun getNetworkInfo() = networkExporter.getNetworkInfo()

    override fun getApplicationInfo() = applicationExporter.getApplicationInfo()
}
