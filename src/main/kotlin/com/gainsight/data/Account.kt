package com.gainsight.data

import javax.persistence.*

@Entity
@Table(name = "Account", schema = "salesforce")
@NamedQueries(
        NamedQuery(
                name = "Account.findBySfid",
                query = "SELECT a FROM Account a WHERE sfid = :sfid"
        ),
        NamedQuery(
                name = "Account.findByName",
                query = "SELECT a FROM Account a WHERE name = :name"
        )
)
class Account(
        val name: String,
        @Column(name = "SAP_CompanyID__c")
        val partnerCustomerNumber: String,
        val sfid: String,
        @Column(name = "Notification_Endpoint__c")
        val endpoint: String
) : AbstractJpaPersistable<Long>()