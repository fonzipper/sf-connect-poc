package com.gainsight.data

import javax.persistence.*

@Entity
@Table(name = "Task", schema = "salesforce")
@NamedQueries(
        NamedQuery(
                name = "Task.findBySfid",
                query = "SELECT t FROM Task t WHERE sfid = :sfid"
        ),
        NamedQuery(
                name = "Task.findBySpon",
                query = "SELECT t FROM Task t WHERE spon = :spon"
        ),
        NamedQuery(
                name = "Task.findByParent",
                query = "SELECT t FROM Task t WHERE parentId = :parentId"
        )
)
class Task(
        val isArchived: Boolean,
        val isDeleted: Boolean,
        val isClosed: Boolean,
        @Column(name = "SPON__c")
        val spon: String,
        val subject: String,
        val sfid: String,
        @Column(name = "WhatId")
        val parentId: String
) : AbstractJpaPersistable<Long>()