package lab.monilabexporterex.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_metric_application")
class ApplicationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 10)
    val label: Label,
    @Column(name = "http_requests_count", nullable = false)
    val httpRequestsCount: Long,
    @Column(name = "http_latency", nullable = false)
    val httpLatency: Double,
    @Column(name = "db_connections_active", nullable = false)
    val dbConnectionsActive: Int,
    @Column(name = "db_connections_max", nullable = false)
    val dbConnectionsMax: Int,
    @Column(name = "queue_tasks_pending", nullable = false)
    val queueTasksPending: Int,
    @Lob
    @Column(name = "custom_metrics", columnDefinition = "TEXT", nullable = false)
    val customMetrics: String,
    @Column(name = "registered_date_time", nullable = false, updatable = false)
    var registeredDateTime: LocalDateTime = LocalDateTime.now(),
)
