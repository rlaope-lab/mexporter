package lab.monilabexporterex.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_metric_network")
class NetworkEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 10)
    val label: Label,

    @Column(name = "bytes_sent", nullable = false)
    val bytesSent: Long,

    @Column(name = "bytes_received", nullable = false)
    val bytesReceived: Long,

    @Column(name = "tcp_connections", nullable = false)
    val tcpConnections: Int,

    @Column(name = "tcp_established", nullable = false)
    val tcpEstablished: Int,

    @Column(name = "open_sockets", nullable = false)
    val openSockets: Int,

    @Column(name = "prefer_ipv4", nullable = false)
    val preferIPv4: Boolean,

    @Column(name = "registered_date_time", nullable = false, updatable = false)
    var registeredDateTime: LocalDateTime = LocalDateTime.now()
)
