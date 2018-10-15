package com.gainsight.controller

import org.apache.commons.dbcp.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by capitan on 9/20/16.
 */

@CrossOrigin(origins = arrayOf("*"))
@RestController
open class GainsightRESTController @Autowired constructor(val dataSource: BasicDataSource) {
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun getMainPage(): String{
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("SELECT * FROM salesforce.workflow__c LIMIT 5")
        val rs = stmt.executeQuery()

        var res = ""
        while (rs.next()){
            res += rs.getString("name") + ','
            println(rs.getString("sfid"))
        }

        return "Hello there $res"
    }

    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.GET))
    fun getWf(@PathVariable("id") id: String): String{
        println("this is id: $id")
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("SELECT * FROM salesforce.Workflow__c WHERE sfid IN (\'$id\')")
        val rs = stmt.executeQuery()

        var res = ""
        while (rs.next()){
            res += rs.getString("name") + ' ' + rs.getString("workflow__c")
            println(rs.getString("sfid"))
        }

        return "Selected workflow $res"
    }
}