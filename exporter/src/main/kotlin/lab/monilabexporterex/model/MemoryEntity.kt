package lab.monilabexporterex.model

import jakarta.persistence.*

@Entity
@Table(name = "tb_metric_memory")
class MemoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false)
    val label: Label,

    @Column(name = "used", nullable = false)
    val used: Long,

    @Column(name = "max", nullable = false)
    val max: Long,

    @Column(name = "committed", nullable = false)
    val committed: Long,

    @Column(name = "eden", nullable = false)
    val eden: Long,

    @Column(name = "survivor", nullable = false)
    val survivor: Long,

    @Column(name = "old", nullable = false)
    val old: Long,

    @Column(name = "buffer_pool_used", nullable = false)
    val bufferPoolUsed: Long,

    @Column(name = "max_direct_memory_size", nullable = false)
    val maxDirectMemorySize: Long,
)
