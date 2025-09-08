package lab.monilabexporterex.monilab

interface JvmExporter {
    fun getMemoryInfo()
    fun getGcInfo()
    fun getThreadInfo()
    fun getCpuInfo()
    fun getClassLoadingInfo()
}