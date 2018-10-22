package com.gainsight.services

import com.gainsight.data.Account
import com.gainsight.data.Keyword
import com.gainsight.data.Notification
import com.gainsight.data.Order
import com.gainsight.intefaces.PartnerNotification
import com.google.gson.Gson
import org.joda.time.DateTime
import java.util.*
import javax.persistence.EntityManager

class SolocalNotification : PartnerNotification {
    override fun doNotification(notification: Notification, entityManager: EntityManager) {
        println("This is Solocal nf")
        val query = entityManager.createNamedQuery("Account.findBySfid", Account::class.java)
        query.setParameter("sfid", notification.partnerId)
        val partner = query.singleResult
        if (partner?.endpoint != null) {
            val payload = getPayload(notification.id, entityManager)
            val pl = Gson().toJson(PayloadWrapper(payload))
            val response = khttp.post(
                    url = partner.endpoint,
                    headers = mapOf("Content-type" to "application/json"),
                    data = pl
            )
            println(response.text)
        }
    }

    private fun getPayload(orderId: Long, entityManager: EntityManager): String {
        var result = ""

        val order = entityManager.find(Order::class.java, orderId)
        if (order != null) {

            val pl = PayLoad()
            pl.partnerOrderNumber = order.orderNumber
            pl.partnerProductNumber = order.productNumber
            pl.partnerCustomerNumber = order.customerId
            pl.spotzerOrderNumber = order.spon
            pl.status = "updated"
            pl.stage = order.stage
            pl.eventDate = DateTime.now().toString()

            val keywordQuery = entityManager.createNamedQuery("Keyword.findByOrder", Keyword::class.java)
            keywordQuery.setParameter("orderId", order.sfid)
            keywordQuery.resultList.forEach{ kw ->
                pl.keywords = pl.keywords.plus(kw.name)
            }

            result = pl.toString()
        }

        return result
    }

    private data class PayloadWrapper(
            val text: String,
            val username: String = "Heroku Notifier",
            val icon_emoji: String = "bug"
    )

    private data class PayLoad(
            var partnerOrderNumber: String = "",
            var partnerProductNumber: String = "",
            var partnerCustomerNumber: String = "",
            var spotzerOrderNumber: String = "",
            var status: String = "",
            var stage: String = "",
            var eventDate: String = "",
            var keywords: Array<String> = arrayOf()
    ) {
        override fun toString(): String {
            return "*partnerOrderNumber*: $partnerOrderNumber \n" +
                    " *partnerProductNumber*: $partnerProductNumber \n" +
                    " *partnerCustomerNumber*: $partnerCustomerNumber \n" +
                    " *spotzerOrderNumber*: $spotzerOrderNumber \n" +
                    " *status*: $status \n" +
                    " *stage*: $stage \n" +
                    " *eventDate*: $eventDate \n" +
                    " *keywords*: ${keywords.joinToString(separator = ", ")}"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PayLoad

            if (partnerOrderNumber != other.partnerOrderNumber) return false
            if (partnerProductNumber != other.partnerProductNumber) return false
            if (partnerCustomerNumber != other.partnerCustomerNumber) return false
            if (spotzerOrderNumber != other.spotzerOrderNumber) return false
            if (status != other.status) return false
            if (stage != other.stage) return false
            if (eventDate != other.eventDate) return false
            if (!Arrays.equals(keywords, other.keywords)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = partnerOrderNumber.hashCode()
            result = 31 * result + partnerProductNumber.hashCode()
            result = 31 * result + partnerCustomerNumber.hashCode()
            result = 31 * result + spotzerOrderNumber.hashCode()
            result = 31 * result + status.hashCode()
            result = 31 * result + stage.hashCode()
            result = 31 * result + eventDate.hashCode()
            result = 31 * result + Arrays.hashCode(keywords)
            return result
        }
    }
}