package com.gainsight.intefaces

import com.gainsight.data.PartnerConfig
import org.springframework.data.jpa.repository.JpaRepository

interface PartnerConfigRepository : JpaRepository<PartnerConfig, Long> {
    fun getByName(name: String): PartnerConfig?
}