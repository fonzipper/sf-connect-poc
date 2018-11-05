package com.gainsight.tasks

import com.gainsight.data.PlatformEvent
import com.gainsight.data.SalesforceConfig
import com.github.wnameless.json.flattener.JsonFlattener
import com.google.gson.Gson
import com.salesforce.emp.connector.BayeuxParameters
import com.salesforce.emp.connector.EmpConnector
import com.salesforce.emp.connector.example.LoggingListener
import org.cometd.bayeux.Channel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
class EMPListener @Autowired constructor(
        private val salesforceConfig: SalesforceConfig,
        private val notificationHandler: NotificationHandler) {

    @PostConstruct
    fun listen() {
        val replayFrom = EmpConnector.REPLAY_FROM_EARLIEST

        val params = object : BayeuxParameters {

            override fun bearerToken(): String {
                return salesforceConfig.token
            }

            override fun host(): URL {
                try {
                    return URL(salesforceConfig.url)
                } catch (e: MalformedURLException) {
                    throw IllegalArgumentException(String.format("Unable to create url: %s", salesforceConfig.url), e)
                }

            }
        }

        val consumer = Consumer<Map<String, Any>> {
            event ->
            run {
                val gson = Gson()
                val platformEvent = gson.fromJson<PlatformEvent>(
                        JsonFlattener.flatten(gson.toJson(event)),
                        PlatformEvent::class.java
                )
                notificationHandler.notifyPartner(platformEvent)
            }
        }
        val connector = EmpConnector(params)

        connector.addListener(Channel.META_CONNECT, LoggingListener(true, true))
                .addListener(Channel.META_DISCONNECT, LoggingListener(true, true))
                .addListener(Channel.META_HANDSHAKE, LoggingListener(true, true))

        connector.start().get(5, TimeUnit.SECONDS)

        val subscription = connector.subscribe(salesforceConfig.topic, replayFrom, consumer).get(5, TimeUnit.SECONDS)

        println(String.format("Subscribed: %s", subscription))
    }
}