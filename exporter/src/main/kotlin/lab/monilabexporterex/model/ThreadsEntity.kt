package lab.monilabexporterex.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_metric_threads")
class ThreadsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 10)
    val label: Label,

    @Column(name = "count", nullable = false)
    val count: Int,

    @Column(name = "daemon_count", nullable = false)
    val daemonCount: Int,

    @Column(name = "peak_count", nullable = false)
    val peakCount: Int,

    @Column(name = "deadlocked_count", nullable = false)
    val deadlockedCount: Int,

    @Column(name = "cpu_time", nullable = false)
    val cpuTime: Long,

    @Lob
    @Column(name = "states", columnDefinition = "TEXT", nullable = false)
    val states: String,

    @Column(name = "regist_date_time", nullable = false, updatable = false)
    var registDateTime: LocalDateTime = LocalDateTime.now()
)
