package com.gainsight.tasks

import com.gainsight.misc.Listener
import org.apache.commons.dbcp.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class WorkflowHandler @Autowired constructor(val dataSource: BasicDataSource) {
    fun handleWorkflowChange() {
        val connection = dataSource.connection
        val listener = Listener(connection)
        listener.start()
    }
}