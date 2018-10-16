package com.gainsight.misc

import org.postgresql.PGConnection
import java.sql.Connection

open class Listener(conn: Connection) : Thread() {
    private val connection = conn

    override fun run() {
        var stmt = connection.createStatement()
        stmt.execute("LISTEN wf_changes")
        stmt.close()

        while (true) {
            stmt = connection.createStatement()
            val rs = stmt.executeQuery("SELECT 1")
            rs.close()
            stmt.close()

            val pgConnection = connection.unwrap(PGConnection::class.java)
            pgConnection.notifications?.forEach { nf ->
                println("FOUND NOTIFICATION ${nf.name} with param ${nf.parameter}")
            }
            Thread.sleep(500)
        }
    }
}