package com.gainsight

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
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

fun getDataSource() : BasicDataSource {
    val uri = URI("postgres://krcumcpuuvobfh:e6165a062fc5141cf2feb7095f1d072e03f271791dd08eb06ae640bb4aed7c17@ec2-54-217-235-166.eu-west-1.compute.amazonaws.com:5432/de9lr0had545p3")
//    val uri = URI(System.getenv("DATABASE_URL"))

    val username = uri.userInfo.split(":")[0]
    val password = uri.userInfo.split(":")[1]
    val url = "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require"

    val db = BasicDataSource()
    db.url = url
    db.username = username
    db.password = password

    return db
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