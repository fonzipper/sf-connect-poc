package com.gainsight.intefaces

import com.gainsight.data.Notification
import javax.persistence.EntityManager

interface PartnerNotification {
    fun doNotification(notification: Notification, entityManager: EntityManager)
}