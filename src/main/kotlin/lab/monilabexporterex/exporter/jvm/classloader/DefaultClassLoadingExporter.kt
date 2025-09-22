package lab.monilabexporterex.exporter.jvm.classloader

import lab.monilabexporterex.exporter.data.JvmMonitoringData
import lab.monilabexporterex.exporter.jvm.ClassLoadingExporter
import org.springframework.stereotype.Component

@Component
class DefaultClassLoadingExporter : ClassLoadingExporter {
    override fun getClassLoadingInfo(): JvmMonitoringData.ClassLoadingInfo {
        TODO("Not yet implemented")
    }

}
