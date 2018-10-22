package com.gainsight.tasks

import com.gainsight.data.Notification
import com.gainsight.data.Order
import com.gainsight.intefaces.PartnerNotification
import com.gainsight.services.NotificationFactory
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Component
class NotificationHandler {
    @PersistenceContext
    lateinit var entityManager: EntityManager

    fun notifyPartner(notification: Notification) {
        val order = entityManager.find(Order::class.java, notification.id)

        val communication: PartnerNotification = NotificationFactory.getCommunication(order.partnerName)
        communication.doNotification(notification, entityManager)
    }
}