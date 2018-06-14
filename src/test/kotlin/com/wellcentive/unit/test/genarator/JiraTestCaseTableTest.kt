package com.wellcentive.unit.test.genarator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Test of [JiraTestCaseTable]
 */
internal open class JiraTestCaseTableTest {

    @Test
    internal fun testLineItemsFromNumeratedTable() {
        val jiraTemplateFilePath = "${UnitTestGeneratorTest.RESOURCE_PATH}${File.separator}${UnitTestGeneratorTest.JIRA_TEMPLATE_FILENAME}"
        val jiraTemplate = getTextFileContent(jiraTemplateFilePath)

        val jiraTestCaseTable = JiraTestCaseTable(jiraTemplate)
        val splitedJiraTable = splitJiraTable(jiraTemplate)
        val lineItems = jiraTestCaseTable.lineItems

        assertEquals(splitedJiraTable.size, lineItems.size)
        assertEquals(splitedJiraTable[0][0].toInt(), lineItems[0].number)
        assertEquals(splitedJiraTable[0][1].trim(), lineItems[0].testCase)
        assertEquals(splitedJiraTable[0][2], lineItems[0].result)
    }

    @Test
    internal fun testLineItemsFromNotNumeratedTable() {
        val jiraTemplateFileName = "JiraTemplateWithoutNumeration.txt"
        val jiraTemplateFilePath = "${UnitTestGeneratorTest.RESOURCE_PATH}${File.separator}$jiraTemplateFileName"
        val jiraTemplate = getTextFileContent(jiraTemplateFilePath)

        val jiraTestCaseTable = JiraTestCaseTable(jiraTemplate)
        val splitedJiraTable = splitJiraTable(jiraTemplate)
        val lineItems = jiraTestCaseTable.lineItems

        assertEquals(splitedJiraTable.size, lineItems.size)
        assertEquals(splitedJiraTable[0][0], lineItems[0].testCase)
        assertEquals(splitedJiraTable[0][1], lineItems[0].result)
    }

    private fun splitJiraTable(jiraTable: String): List<List<String>> {
        return jiraTable
                .split("\n")
                .asSequence()
                .map { it.substring(1, it.length - 1) }
                .map { it.split("|") }
                .toList()
    }
}