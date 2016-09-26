package com.gainsight

import org.apache.commons.dbcp.BasicDataSource
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.net.URI

/**
 * Created by capitan on 9/16/16
 */

@SpringBootApplication
open class Application {
    @Bean open fun dataSource() = getDataSource()
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

fun getDataSource() : BasicDataSource {
//    val uri = URI("postgres://azellloqqlrnpv:13vu13-kVHG6cLw8Qwaj-_pFat@ec2-54-243-231-255.compute-1.amazonaws.com:5432/d5cjbi2fceh6tl")
    val uri = URI(System.getenv("DATABASE_URL"))
    val username = uri.userInfo.split(":")[0]
    val password = uri.userInfo.split(":")[1]
    val url = "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path

    val db = BasicDataSource()
    db.url = url
    db.username = username
    db.password = password

    return db
}

@Configuration
@EnableWebMvc
open class WebConfig: WebMvcConfigurerAdapter(){
    @Override
    override fun addCorsMappings(registry: CorsRegistry?) {
        registry?.addMapping("/**")
        super.addCorsMappings(registry)
    }
}