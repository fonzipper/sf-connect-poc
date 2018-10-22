package com.gainsight.data

import javax.persistence.*

@Entity
@Table(name = "CF_Purchase_Order__c", schema = "salesforce")
@NamedQueries(
    NamedQuery(
            name = "Order.findBySfid",
            query = "SELECT o FROM Order o WHERE sfid = :sfid"
    ),
    NamedQuery(
            name = "Order.findBySpon",
            query = "SELECT o FROM Order o WHERE spon = :spon"
    )
)
class Order(
        @Column(name = "Customer__c")
        val customerId: String,
        @Column(name = "External_Order_Number__c")
        val orderNumber: String,
        val sfid: String,
        @Column(name = "Partner_Product_Number__c")
        val productNumber: String,
        @Column(name = "Partner__c")
        val partnerId: String,
        @Column(name = "Previous_Stage__c")
        val previousStage: String,
        @Column(name = "SPON__c")
        val spon: String,
        @Column(name = "Stage__c")
        val stage: String,
        @Column(name = "Price_Book__c")
        val partnerName: String
) : AbstractJpaPersistable<Long>()