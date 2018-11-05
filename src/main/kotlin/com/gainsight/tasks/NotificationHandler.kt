package com.gainsight.tasks

import com.gainsight.data.PlatformEvent
import com.gainsight.intefaces.PartnerNotification
import com.gainsight.services.NotificationFactory
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Component
class NotificationHandler {
    @PersistenceContext
    lateinit var entityManager: EntityManager

//    fun notifyPartner(notification: Notification) {
//        val order = entityManager.find(Order::class.java, notification.id)
//
//        val communication: PartnerNotification = NotificationFactory.getCommunication(order.partnerName)
//        communication.doNotification(notification, entityManager)
//    }

    @Transactional
    fun notifyPartner(event: PlatformEvent) {
        val query = entityManager.createNamedQuery("PlatformEvent.findByReplayId", PlatformEvent::class.java)
        query.setParameter("replayId", event.eventReplay)
        var platformEvent : PlatformEvent? = null
        try {
            platformEvent = query.singleResult
        } catch (e: NoResultException) {
            println("Platform event not found in database for replayId ${event.eventReplay}")
        }

        val communication: PartnerNotification = NotificationFactory.getCommunication(event.partnerName)
        if (platformEvent == null) {
            event.successful = communication.doNotification(event)
            entityManager.persist(event)
        } else if (!platformEvent.successful) {
            platformEvent.successful = communication.doNotification(event)
            entityManager.merge(platformEvent)
        }
    }
}