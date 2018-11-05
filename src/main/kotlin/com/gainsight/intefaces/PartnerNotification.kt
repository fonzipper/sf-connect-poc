package com.gainsight.intefaces

import com.gainsight.data.PlatformEvent

interface PartnerNotification {
    fun doNotification(event: PlatformEvent) : Boolean
}