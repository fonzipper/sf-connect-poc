package com.gainsight.tasks

import org.apache.commons.dbcp.BasicDataSource
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by capitan on 9/26/16
 */

@Component
open class CaseCounter @Autowired constructor(val dataSource: BasicDataSource) {


    @Scheduled(fixedRate = 86400000)
    fun updateCaseCounter(){
        val dt = DateTime.now().minusDays(30).toString("yyyy-MM-dd")
        val conn = dataSource.connection
        val caseQuery = "SELECT * FROM salesforce.case WHERE CreatedDate > \'$dt\'"
//        val caseQuery = "SELECT * FROM salesforce.case LIMIT 1"
        println(caseQuery)
        val caseStatement = conn.prepareStatement(caseQuery)
        val caseResult = caseStatement.executeQuery()

//        println("cases found: " + caseResult.getString("count"))

//        if (caseResult.fetchSize > 0) {
//        println(caseResult.getString("CreatedDate"))
        val idToCountMap = HashMap<String, Int>()
        var doProcess  = false
        var i = 0
        while (caseResult.next() && i < 10) {
            i++
            doProcess = true
            val id = caseResult.getString("AccountId")
            if (idToCountMap.containsKey(id))
                idToCountMap.put(id, idToCountMap[id]!!.plus(1))
            else
                idToCountMap.put(id, 1)
        }
//        val q = "($$0018000000uueCCAAY&&,$$0018000001J3wwjAAB&&,$$0018000000wnrdYAAQ&&,$$0018000001EtUfgAAF&&,$$00180000015z1DjAAI&&,$$00180000011K2RlAAK&&,$$00180000019dgb8AAA&&,$$0018000000xMGxcAAG&&,$$0018000000o3Pc3AAE$$)"
        if (doProcess) {
            val ids = idToCountMap.keys.joinToString ("\' , \'", "(\'", "\')", -1, "...")
            println("ids: $ids")
            var usageQuery = "SELECT * FROM salesforce.JBCXM__UsageData__c WHERE JBCXM__Account__c IN $ids"

//            for (key in idToCountMap.keys){
//                usageQuery += " JBCXM__Account__c = \"$key\" OR "
//            }
//            usageQuery = usageQuery.substring(0, usageQuery.length-4)
            println(usageQuery)
            val usageStatement = conn.prepareStatement(usageQuery)
            val usageResult = usageStatement.executeQuery()

    //            println("usage docs found: " + usageResult.fetchSize)

            var query = "UPDATE salesforce.JBCXM__UsageData__c"
            while (usageResult.next()) {
                val accId = usageResult.getString("JBCXM__Account__c")
                val recordId = usageResult.getString("id")
                query += " SET Cases_Created__c = ${idToCountMap[accId]} WHERE ID = $recordId"
            }

            val updateStatement = conn.prepareStatement(query)
            val result = updateStatement.execute()

            if (result)
                println("update succeeded")
            else
                println("update failed")
        }
    }
}