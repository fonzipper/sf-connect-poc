package com.gainsight.controller

import org.apache.commons.dbcp.BasicDataSource
import org.postgresql.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.sql.CommonDataSource

/**
 * Created by capitan on 9/20/16.
 */

@CrossOrigin(origins = arrayOf("*"))
@RestController
open class GainsightRESTController @Autowired constructor(val dataSource: BasicDataSource) {
    @RequestMapping(value = "/**", method = arrayOf(RequestMethod.GET))
    fun getMainPage(): String{
        val conn = dataSource.connection
        var stmt = conn.prepareStatement("SELECT * FROM salesforce.case MAX 1")
        val rs = stmt.executeQuery()

        return "Hello there"
    }
}