package com.gainsight.data

import com.google.gson.annotations.SerializedName
import javax.persistence.Entity
import javax.persistence.NamedQuery

@Entity
@NamedQuery(
        name = "PlatformEvent.findByReplayId",
        query = "SELECT pe FROM PlatformEvent pe WHERE eventReplay = :replayId"
)
class PlatformEvent : AbstractJpaPersistable<Long>() {
    val schema: String = ""
    @SerializedName("payload.CreatedDate")
    val createdDate: String = ""
    @SerializedName("payload.Keywords__c")
    val keywords: String = ""
    @SerializedName("payload.Partner_Name__c")
    val partnerName: String = ""
    @SerializedName("payload.Spotzer_Order_Number__c")
    val spotzerOrderNumber: String = ""
    @SerializedName("payload.Partner_Customer_Number__c")
    val partnerCustomerNumber: String = ""
    @SerializedName("payload.Partner_Order_Number__c")
    val partnerOrderNumber: String = ""
    @SerializedName("payload.Partner_Product_Number__c")
    val partnerProductNumber: String = ""
    @SerializedName("payload.Stage__c")
    val stage: String = ""
    @SerializedName("payload.Status__c")
    val status: String = ""
    @SerializedName("payload.Endpoint__c")
    val endpoint: String = ""
    @SerializedName("event.replayId")
    val eventReplay: Long = 0
    var successful: Boolean = false
}

//@Entity
//@Table(name = "event_payload")
//class EventPayload : AbstractJpaPersistable<Long>() {
//    @SerializedName("CreatedDate")
//    val createdDate: String = ""
//    @SerializedName("Keywords__c")
//    val keywords: String = ""
//    @SerializedName("Spotzer_Order_Number__c")
//    val spotzerOrderNumber: String = ""
//    @SerializedName("Partner_Customer_Number__c")
//    val partnerCustomerNumber: String = ""
//    @SerializedName("Partner_Order_Number__c")
//    val partnerOrderNumber: String = ""
//    @SerializedName("Partner_Product_Number__c")
//    val partnerProductNumber: String = ""
//    @SerializedName("Stage__c")
//    val stage: String = ""
//    @SerializedName("Status__c")
//    val status: String = ""
//}