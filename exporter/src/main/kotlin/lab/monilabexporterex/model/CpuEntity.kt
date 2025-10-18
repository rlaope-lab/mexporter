package lab.monilabexporterex.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_metric_cpu")
class CpuEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 10)
    val label: Label,

    @Column(name = "process_usage", nullable = false)
    val processUsage: Double,

    @Column(name = "system_usage", nullable = false)
    val systemUsage: Double,

    @Column(name = "uptime", nullable = false)
    val uptime: Long,

    @Column(name = "start_time", nullable = false)
    val startTime: Long,

    @Column(name = "load_average", nullable = false)
    val loadAverage: Double,

    @Column(name = "open_fds", nullable = false)
    val openFds: Long,

    @Column(name = "regist_date_time", nullable = false, updatable = false)
    var registDateTime: LocalDateTime = LocalDateTime.now()
)
