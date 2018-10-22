package com.gainsight.intefaces

import com.gainsight.data.Workflow
import org.springframework.data.jpa.repository.JpaRepository

interface WorkflowRepository : JpaRepository<Workflow, Long> {
    fun getBySfid(sfid: String) : Workflow?
}