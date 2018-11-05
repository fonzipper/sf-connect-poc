package com.gainsight

import com.gainsight.data.SalesforceConfig
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.net.URI

/**
 * Created by capitan on 9/16/16
 */

@SpringBootApplication
@EnableScheduling
class Application {
    @Bean
    fun dataSource() = getDataSource()
    @Bean
    fun salesforceConfig() = getSalesforceConfig()
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

fun getDataSource() : BasicDataSource {
    val uri = URI(System.getenv("DATABASE_URL"))

    val username = uri.userInfo.split(":")[0]
    val password = uri.userInfo.split(":")[1]
    val url = "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require"

    val db = BasicDataSource()
    db.url = url
    db.username = username
    db.password = password

    return db
}

fun getSalesforceConfig() : SalesforceConfig {
    val clientId = System.getenv("SALESFORCE_CLIENTID")
    val secret = System.getenv("SALESFORCE_SECRET")
    val instanceUrl = System.getenv("SALESFORCE_URL")
    val login = System.getenv("SALESFORCE_LOGIN")
    val password = System.getenv("SALESFORCE_PASSWORD")

    println("-------------------------------------------------------------------------------")
    println(clientId)
    println(secret)
    println(instanceUrl)
    println(login)
    println(password)
    println("-------------------------------------------------------------------------------")

    val response = khttp.post(
            url = "$instanceUrl/services/oauth2/token",
            data = mapOf(
                    "grant_type" to "password",
                    "client_id" to clientId,
                    "client_secret" to secret,
                    "redirect_uri" to "http://localhost:8080/oauth2/callback",
                    "username" to login,
                    "password" to password
            )
    )

    println("-------------------------------------------------------------------------------")
    println(response.text)
    println(response.statusCode)
    println(response.jsonObject)
    println("-------------------------------------------------------------------------------")

    return SalesforceConfig(
            url = response.jsonObject.get("instance_url").toString(),
            token = response.jsonObject.get("access_token").toString(),
            topic = System.getenv("SALESFORCE_TOPIC")
    )
}

@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurerAdapter(){
    @Override
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
        super.addCorsMappings(registry)
    }
}