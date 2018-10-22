package com.gainsight.tasks

import com.gainsight.misc.Listener
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class WorkflowHandler @Autowired constructor(
        private val dataSource: BasicDataSource,
        private val notificationHandler: NotificationHandler
) {
    @PostConstruct
    fun handleWorkflowChange() {
        println("started to listen!")
        val connection = dataSource.connection
        val listener = Listener(connection, notificationHandler)
        listener.start()
    }
}