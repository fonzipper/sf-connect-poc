package com.gainsight.misc

import com.gainsight.tasks.NotificationHandler
import java.sql.Connection

open class Listener(conn: Connection, notificationHandler: NotificationHandler) : Thread() {
    private val connection = conn
    private val notifier = notificationHandler

    override fun run() {
        var stmt = connection.createStatement()
        stmt.execute("LISTEN order_update")
        stmt.close()

//        while (true) {
//            stmt = connection.createStatement()
//            val rs = stmt.executeQuery("SELECT 1")
//            rs.close()
//            stmt.close()
//
//            val pgConnection = connection.unwrap(PGConnection::class.java)
//            pgConnection.notifications?.forEach { nf ->
//                println("FOUND NOTIFICATION ${nf.name} with param ${nf.parameter}")
//                val notification = Gson().fromJson<Notification>(nf.parameter, Notification::class.java)
//                notifier.notifyPartner(notification)
//            }
//            Thread.sleep(500)
//        }
    }
}