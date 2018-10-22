package com.gainsight.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Workflow__c", schema = "salesforce")
class Workflow(
        val name: String,
        @Column(name = "Workflow__c")
        val workflow: String,
        @Column(name = "assign_to__c")
        val assignTo: String,
        @Column(name = "task_from__c")
        val taskFrom: String,
        val sfid: String
) : AbstractJpaPersistable<Long>()