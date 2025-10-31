package lab.monilabexporterex.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_metric_class_loading")
class ClassLoadingInfoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 10)
    val label: Label,
    @Column(name = "loaded", nullable = false)
    val loaded: Int,
    @Column(name = "unloaded", nullable = false)
    val unloaded: Long,
    @Column(name = "code_cache_used", nullable = false)
    val codeCacheUsed: Long,
    @Column(name = "code_cache_max", nullable = false)
    val codeCacheMax: Long,
    @Column(name = "compilation_time", nullable = false)
    val compilationTime: Long,
    @Column(name = "reserved_code_cache_size", nullable = false)
    val reservedCodeCacheSize: Long,
    @Column(name = "registered_date_time", nullable = false, updatable = false)
    var registeredDateTime: LocalDateTime = LocalDateTime.now(),
)
