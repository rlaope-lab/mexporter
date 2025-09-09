package lab.monilabexporterex.exporter

import lab.monilabexporterex.exporter.jvm.gc.DefaultGcExporter
import lab.monilabexporterex.exporter.jvm.memory.DefaultMemoryExporter
import lab.monilabexporterex.exporter.jvm.thread.DefaultThreadExporter
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExporterTest {

    @Autowired
    lateinit var memoryExporter: DefaultMemoryExporter

    @Autowired
    lateinit var gcExporter: DefaultGcExporter

    @Autowired
    lateinit var threadExporter: DefaultThreadExporter

    private val log = LoggerFactory.getLogger(ExporterTest::class.java)

    @Test
    fun printJvmMetrics() {
        repeat(5) { i ->
            val memory = memoryExporter.getMemoryInfo()
            val gc = gcExporter.getGcInfo()
            val threads = threadExporter.getThreadInfo()

            log.info("==== Sample #$i ====")

            log.info(
                "[Memory] used={} max={} committed={} eden={} survivor={} old={} bufferPool={} maxDirect={}",
                memory.used, memory.max, memory.committed,
                memory.eden, memory.survivor, memory.old,
                memory.bufferPoolUsed, memory.maxDirectMemorySize
            )

            log.info(
                "[GC] count={} time={}ms pause={}ms allocRate={}B/s liveData={} strategy={}",
                gc.count, gc.time, gc.pause, gc.allocationRate, gc.liveDataSize, gc.gcStrategy
            )

            log.info(
                "[Threads] count={} daemon={} peak={} deadlocked={} cpuTime={} states={}",
                threads.count, threads.daemonCount, threads.peakCount,
                threads.deadlockedCount, threads.cpuTime, threads.states
            )

            Thread.sleep(1000) // 1초 간격
        }
    }
}
