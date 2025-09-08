package lab.monilabexporterex.exporter.jvm

import lab.monilabexporterex.exporter.data.JvmMonitoringData

interface MemoryExporter {
    fun getMemoryInfo(): JvmMonitoringData.Memory
}

interface GcExporter {
    fun getGcInfo(): JvmMonitoringData.Gc
}

interface ThreadExporter {
    fun getThreadInfo(): JvmMonitoringData.Threads
}

interface CpuExporter {
    fun getCpuInfo(): JvmMonitoringData.Cpu
}

interface ClassLoadingExporter {
    fun getClassLoadingInfo(): JvmMonitoringData.ClassLoadingInfo
}

interface NetworkExporter {
    fun getNetworkInfo(): JvmMonitoringData.Network
}

interface ApplicationExporter {
    fun getApplicationInfo(): JvmMonitoringData.Application
}