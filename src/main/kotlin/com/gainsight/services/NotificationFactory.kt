package com.gainsight.services

import com.gainsight.intefaces.PartnerNotification
import org.springframework.stereotype.Component

@Component
class NotificationFactory {
    companion object {
        fun getCommunication(partner: String) : PartnerNotification {
            return when (partner) {
                "Solocal Marketing Services" -> SolocalNotification()
                else -> GenericNotification()
            }
        }
    }
}