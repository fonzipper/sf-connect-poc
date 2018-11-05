package com.gainsight.controller

import com.gainsight.intefaces.PartnerConfigRepository
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by capitan on 9/20/16.
 */

@CrossOrigin(origins = ["*"])
@RestController
class GainsightRESTController @Autowired constructor(
        private val dataSource: BasicDataSource,
        private val repo: PartnerConfigRepository
) {
    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun getMainPage(): String{
//        val conn = dataSource.connection
//        val stmt = conn.prepareStatement("SELECT * FROM salesforce.workflow__c LIMIT 5")
//        val rs = stmt.executeQuery()
//
//        var res = ""
//        while (rs.next()){
//            res += rs.getString("name") + ','
//            println(rs.getString("sfid"))
//        }

        return "Hello there"
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getWf(@PathVariable("id") id: String): String{
//        println("this is id: $id")
//        val conn = dataSource.connection
//        val stmt = conn.prepareStatement("SELECT * FROM salesforce.Workflow__c WHERE sfid LIKE \'%$id%\'")
//        val rs = stmt.executeQuery()
//
//        var res = ""
//        while (rs.next()){
//            res += rs.getString("name") + ' ' + rs.getString("workflow__c")
//            println(rs.getString("sfid"))
//        }

        return "Selected workflow"
    }

    @RequestMapping(value = ["/partner"], method = [RequestMethod.GET])
    fun createPartner(
            @RequestParam("name", required = false) name: String?,
            @RequestParam("url", required = false) url: String?): String {
//        if (name == null) return "Parameters has not been provided"
//        var partner = repo.getByName(name)
//        if (url == null) {
//            return partner.toString() + "<br/>" + partner?.endpoint
//        }
//        if (partner == null) {
//            partner = PartnerConfig(name, url)
//        } else {
//            partner.endpoint = url
//        }
//        repo.save(partner)

        return "Oh, hi"
    }

    @RequestMapping(value = ["/oauth2/callback"], method = [RequestMethod.GET, RequestMethod.POST])
    fun handleAuth(@RequestBody result: String) {
        println(result)
    }
}