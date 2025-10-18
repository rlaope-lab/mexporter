package lab.monilabexporterex.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_metric_gc")
class GcEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 10)
    val label: Label,

    @Column(name = "count", nullable = false)
    val count: Long,

    @Column(name = "time", nullable = false)
    val time: Long,

    @Column(name = "pause", nullable = false)
    val pause: Long,

    @Column(name = "allocation_rate", nullable = false)
    val allocationRate: Double,

    @Column(name = "live_data_size", nullable = false)
    val liveDataSize: Long,

    @Column(name = "gc_strategy", nullable = false)
    val gcStrategy: String,

    @Column(name = "registered_date_time", nullable = false, updatable = false)
    var registeredDateTime: LocalDateTime = LocalDateTime.now()
)
