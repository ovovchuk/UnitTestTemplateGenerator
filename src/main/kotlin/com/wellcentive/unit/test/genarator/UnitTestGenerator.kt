package com.wellcentive.unit.test.genarator

import java.io.FileWriter

/**
 * For generation of unit test templates from JIRA table.
 */
class UnitTestGenerator {

    fun generate(jiraTemplate: String): String {
        val jiraTestCaseTable = JiraTestCaseTable(jiraTemplate)
        return jiraTestCaseTable.lineItems.asSequence()
                .map { convertLineItemToUnitTestTemplate(it) }
                .joinToString("\n\n")
    }

    private fun convertLineItemToUnitTestTemplate(lineItem: JiraTestCaseLineItem): String {
        return """@Test
@DisplayName(
    value = "${lineItem.testCase}",
    expect = "${lineItem.result}"
)
public void testCase${lineItem.number}() throws Exception { //NOSONAR

}"""
    }

    fun writeToFile(filePath: String, content: String) {
        FileWriter(filePath).use {
            it.write(content)
            it.close()
        }
    }
}

class JiraTestCaseTable(jiraTemplate: String) {
    companion object {
        const val LINE_DELIMITER = "|--|"

        fun sanitizeTemplate(jiraTemplate: String): String {
            val lineDelimiter = JiraTestCaseTable.LINE_DELIMITER
            val space = " "
            val newLine = "\n"
            val pipe = "|"
            return jiraTemplate
                    .replace("$pipe$newLine", "$pipe$lineDelimiter")
                    .replace(newLine, space)
        }
    }

    val lineItems: List<JiraTestCaseLineItem>
    var count = 1

    init {
        lineItems = createLineItems(jiraTemplate)
    }

    private fun createLineItems(jiraTemplate: String): List<JiraTestCaseLineItem> =
            sanitizeTemplate(jiraTemplate)
                    .split(JiraTestCaseTable.LINE_DELIMITER)
                    .asSequence()
                    .map(this::removeFirstAndLastPipesFromLine)
                    .map { it.split("|") }
                    .map(this::trimValues)
                    .map(this::mapToJiraTestCaseLineItem)
                    .toList()

    private fun removeFirstAndLastPipesFromLine(line: String): String {
        val startIndex = 1
        val endIndex = line.length - 1
        return line.substring(startIndex, endIndex)
    }

    private fun trimValues(values: List<String>): List<String> = values.map { it.trim() }

    private fun mapToJiraTestCaseLineItem(values: List<String>): JiraTestCaseLineItem {
        return if (isTableWithoutNumeration(values)) createLineItemFromTable(values) else createLineItemFromNumeratedTable(values)
    }

    private fun createLineItemFromNumeratedTable(values: List<String>): JiraTestCaseLineItem {
        val numberIndex = 0
        val testCaseIndex = 1
        val resultIndex = 2
        return JiraTestCaseLineItem(
                values[numberIndex].toInt(),
                values[testCaseIndex],
                values[resultIndex]
        )
    }

    private fun createLineItemFromTable(values: List<String>): JiraTestCaseLineItem {
        val testCaseIndex = 0
        val resultIndex = 1
        return JiraTestCaseLineItem(
                count++,
                values[testCaseIndex],
                values[resultIndex])
    }

    private fun isTableWithoutNumeration(values: List<String>) = values.size == 2
}

data class JiraTestCaseLineItem(val number: Int, val testCase: String, val result: String)