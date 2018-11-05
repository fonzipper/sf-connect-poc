package com.gainsight.services

import com.gainsight.data.PlatformEvent
import com.gainsight.intefaces.PartnerNotification
import com.google.gson.Gson
import org.joda.time.DateTime

class SolocalNotification : PartnerNotification {
    override fun doNotification(event: PlatformEvent) : Boolean {
        println("This is Solocal nf")
        if (event.endpoint.isNotBlank()) {
            val payload = getPayload(event)
            val pl = Gson().toJson(PayloadWrapper(payload))
            val response = khttp.post(
                    url = event.endpoint,
                    headers = mapOf("Content-type" to "application/json"),
                    data = pl
            )
            println(response.text)

            return response.statusCode in 200..299
        }

        return false
    }

    private fun getPayload(event: PlatformEvent): String {
        val pl = PayLoad()
        pl.partnerOrderNumber = event.partnerOrderNumber
        pl.partnerProductNumber = event.partnerProductNumber
        pl.partnerCustomerNumber = event.partnerCustomerNumber
        pl.spotzerOrderNumber = event.spotzerOrderNumber
        pl.status = event.status
        pl.stage = event.stage
        pl.eventDate = DateTime.parse(event.createdDate).toString("dd:MM:yyyy hh:mm")

        if (event.keywords.isNotBlank()) {
            event.keywords.split(",").forEach{ kw ->
                pl.keywords = pl.keywords.plus(kw.trim())
            }
        }

        return pl.toString()
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
    }
}