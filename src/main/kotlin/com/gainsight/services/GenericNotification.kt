package com.gainsight.services

import com.gainsight.data.Account
import com.gainsight.data.Notification
import com.gainsight.data.Order
import com.gainsight.intefaces.PartnerNotification
import com.google.gson.Gson
import org.joda.time.DateTime
import javax.persistence.EntityManager

class GenericNotification : PartnerNotification {
    override fun doNotification(notification: Notification, entityManager: EntityManager) {
        println("This is Generic nf")
        val query = entityManager.createNamedQuery("Account.findBySfid", Account::class.java)
        query.setParameter("sfid", notification.partnerId)
        val partner = query.singleResult
        println(partner)
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

    fun getPayload(orderId: Long, entityManager: EntityManager): String {
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
            var eventDate: String = ""
    ) {
        override fun toString(): String {
            return "partnerOrderNumber: $partnerOrderNumber \n" +
                    " partnerProductNumber: $partnerProductNumber \n" +
                    " partnerCustomerNumber: $partnerCustomerNumber \n" +
                    " spotzerOrderNumber: $spotzerOrderNumber \n" +
                    " status: $status \n" +
                    " stage: $stage \n" +
                    " eventDate: $eventDate \n"
        }
    }
}