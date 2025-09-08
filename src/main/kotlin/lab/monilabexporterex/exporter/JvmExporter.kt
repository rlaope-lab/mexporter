package lab.monilabexporterex.exporter

import lab.monilabexporterex.exporter.data.JvmMonitoringData

interface JvmExporter {
    fun getHostname(): String
    fun getMemoryInfo(): JvmMonitoringData.Memory
    fun getGcInfo(): JvmMonitoringData.Gc
    fun getThreadInfo(): JvmMonitoringData.Threads
    fun getCpuInfo(): JvmMonitoringData.Cpu
    fun getClassLoadingInfo(): JvmMonitoringData.ClassLoadingInfo
    fun getNetworkInfo(): JvmMonitoringData.Network
    fun getApplicationInfo(): JvmMonitoringData.Application
}
