package com.gainsight.data

import javax.persistence.*

@Entity
@Table(name = "SEO_Keyword__c", schema = "salesforce")
@NamedQueries(
        NamedQuery(
                name = "Keyword.findBySfid",
                query = "SELECT k FROM Keyword k WHERE sfid = :sfid"
        ),
        NamedQuery(
                name = "Keyword.findByOrder",
                query = "SELECT k FROM Keyword k WHERE orderId = :orderId"
        )
)
class Keyword(
        @Column(name = "Keyword__c")
        val name: String,
        @Column(name = "Active__c")
        val isActive: Boolean,
        @Column(name = "External_Id__c")
        val externalId: String,
        @Column(name = "Order__c")
        val orderId: String,
        @Column(name = "SQLID__C")
        val sqlId: String,
        val sfid: String
) : AbstractJpaPersistable<Long>()