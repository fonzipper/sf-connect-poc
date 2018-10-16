package com.gainsight.misc

import org.postgresql.PGConnection
import java.sql.Connection

open class Listener(conn: Connection) : Thread() {
    private val connection = conn
    private val pgConnection = conn as PGConnection

    override fun run() {
        while (true) {
            val stmt = connection.createStatement()
            val rs = stmt.executeQuery("SELECT 1")
            rs.close()
            stmt.close()

            pgConnection.notifications.forEach { nf ->
                println("FOUND NOTIFICATION ${nf.name} with param ${nf.parameter}")
            }
            Thread.sleep(500)
        }
    }
}