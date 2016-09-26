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
        while (caseResult.next()) {
            val id = caseResult.getString("AccountId")
            if (id?.length == 18) {
                doProcess = true
                if (idToCountMap.containsKey(id))
                    idToCountMap.put(id, idToCountMap[id]!!.plus(1))
                else
                    idToCountMap.put(id, 1)
            }
        }
        if (doProcess) {
            val ids = idToCountMap.keys.joinToString ("\' , \'", "(\'", "\')", -1, "...")
            println("ids: $ids")
            val usageQuery = "SELECT * FROM salesforce.JBCXM__UsageData__c WHERE JBCXM__Account__c IN $ids"

            println(usageQuery)
            val usageStatement = conn.prepareStatement(usageQuery)
            val usageResult = usageStatement.executeQuery()

            var valuesMap = ""
            while (usageResult.next()) {
                val accId = usageResult.getString("JBCXM__Account__c")
                val recordId = usageResult.getString("id")
                valuesMap += "($recordId, ${idToCountMap[accId]}),"
            }
            valuesMap = valuesMap.substring(0, valuesMap.length-1)
            val query = "UPDATE salesforce.JBCXM__UsageData__c " +
                    "AS ud SET Cases_Created__c = u.Cases_Created__c " +
                    "FROM (VALUES $valuesMap) AS u(id, Cases_Created__c) " +
                    "WHERE u.id = ud.id"

            println(query)

            val updateStatement = conn.prepareStatement(query)
            val result = updateStatement.execute()

            if (result)
                println("update succeeded")
            else
                println("update failed")
        }
    }
}