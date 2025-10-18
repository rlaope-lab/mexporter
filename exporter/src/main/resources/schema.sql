CREATE TABLE IF NOT EXISTS tb_metric_memory (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  label VARCHAR(10) NOT NULL,
  used BIGINT NOT NULL,
  max BIGINT NOT NULL,
  committed BIGINT NOT NULL,
  eden BIGINT NOT NULL,
  survivor BIGINT NOT NULL,
  old BIGINT NOT NULL,
  buffer_pool_used BIGINT NOT NULL,
  max_direct_memory_size BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_metric_gc (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  label VARCHAR(10) NOT NULL,
  count BIGINT NOT NULL,
  time BIGINT NOT NULL,
  pause BIGINT NOT NULL,
  allocation_rate DOUBLE NOT NULL,
  live_data_size BIGINT NOT NULL,
  gc_strategy VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_metric_threads (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   label VARCHAR(10) NOT NULL,
   count INT NOT NULL,
   daemon_count INT NOT NULL,
   peak_count INT NOT NULL,
   deadlocked_count INT NOT NULL,
   cpu_time BIGINT NOT NULL,
   states TEXT NOT NULL
);



CREATE TABLE IF NOT EXISTS tb_metric_cpu (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   label VARCHAR(10) NOT NULL,
   process_usage DOUBLE NOT NULL,
   system_usage DOUBLE NOT NULL,
   uptime BIGINT NOT NULL,
   start_time BIGINT NOT NULL,
   load_average DOUBLE NOT NULL,
   open_fds BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_metric_network (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(10) NOT NULL,
    bytes_sent BIGINT NOT NULL,
    bytes_received BIGINT NOT NULL,
    tcp_connections INT NOT NULL,
    tcp_established INT NOT NULL,
    open_sockets INT NOT NULL,
    prefer_ipv4 BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_metric_class_loading (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(10) NOT NULL,
    loaded INT NOT NULL,
    unloaded BIGINT NOT NULL,
    code_cache_used BIGINT NOT NULL,
    code_cache_max BIGINT NOT NULL,
    compilation_time BIGINT NOT NULL,
    reserved_code_cache_size BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_metric_class_loading (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     label VARCHAR(10) NOT NULL,
     loaded INT NOT NULL,
     unloaded BIGINT NOT NULL,
     code_cache_used BIGINT NOT NULL,
     code_cache_max BIGINT NOT NULL,
     compilation_time BIGINT NOT NULL,
     reserved_code_cache_size BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_metric_application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(10) NOT NULL,
    http_requests_count BIGINT NOT NULL,
    http_latency DOUBLE NOT NULL,
    db_connections_active INT NOT NULL,
    db_connections_max INT NOT NULL,
    queue_tasks_pending INT NOT NULL,
    custom_metrics TEXT NOT NULL
);
