package com.gainsight.tasks

import org.apache.commons.dbcp.BasicDataSource
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.Date
import java.util.*

/**
 * Created by capitan on 9/26/16.
 */

@Component
open class CaseCounter @Autowired constructor(val dataSource: BasicDataSource) {


    @Scheduled(fixedRate = 86400000)
    fun updateCaseCounter(){
        val dt = DateTime.now().minusDays(100).toString("yyyy-MM-dd")
        val conn = dataSource.connection
        val caseStatement = conn.prepareStatement("SELECT * FROM salesforce.case WHERE CreatedDate > \'$dt\'")
        val caseResult = caseStatement.executeQuery()

        println("files found: " + caseResult.fetchSize)

        if (caseResult.fetchSize > 0) {
            val idToCountMap = HashMap<String, Int>()
            while (caseResult.next()) {
                val id = caseResult.getString("AccountId")
                if (idToCountMap.containsKey(id))
                    idToCountMap.put(id, idToCountMap[id]!!.plus(1))
                else
                    idToCountMap.put(id, 1)
            }

            val usageStatement = conn.prepareStatement("SELECT * FROM salesforce.JBCXM__UsageData__c WHERE sfid IN ${idToCountMap.keys.toTypedArray()}")
            val usageResult = usageStatement.executeQuery()

            println("usage docs found: " + usageResult.fetchSize)

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