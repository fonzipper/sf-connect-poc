package com.gainsight.data

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "task_names__c", schema = "salesforce")
class TaskName(
        val name: String,
        val sfid: String
) : AbstractJpaPersistable<Long>()