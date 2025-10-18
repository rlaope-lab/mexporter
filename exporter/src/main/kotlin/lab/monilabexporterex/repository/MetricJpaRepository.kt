package lab.monilabexporterex.repository

import lab.monilabexporterex.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemoryRepository : JpaRepository<MemoryEntity, Long>

@Repository
interface GcRepository : JpaRepository<GcEntity, Long>

@Repository
interface ThreadsRepository : JpaRepository<ThreadsEntity, Long>

@Repository
interface CpuRepository : JpaRepository<CpuEntity, Long>

@Repository
interface NetworkRepository : JpaRepository<NetworkEntity, Long>

@Repository
interface ClassLoadingInfoRepository : JpaRepository<ClassLoadingInfoEntity, Long>

@Repository
interface ApplicationRepository : JpaRepository<ApplicationEntity, Long>
