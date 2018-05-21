package com.wellcentive.unit.test.genarator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.util.stream.Collectors

internal class UnitTestGeneratorTest {
    internal companion object {
        const val RESOURCE_PATH = "src/test/resources"
        const val JIRA_TEMPLATE_FILENAME = "JiraTemplate.txt"
        const val JIRA_TEMPLATE_WITHOUT_NUMERATION_FILENAME = "JiraTemplateWithoutNumeration.txt"
        const val EXPECTED_UNIT_TEST_TEMPLATE_FILENAME = "ExpectedUnitTestTemplate.txt"
        const val UNIT_TEST_TEMPLATE_FILENAME = "UnitTestTemplate.txt"
    }

    @Test
    internal fun generateTest() {
        val jiraTemplateFilePath = "$RESOURCE_PATH${File.separator}$JIRA_TEMPLATE_FILENAME"
        val jiraTemplate = getTextFileContent(jiraTemplateFilePath)

        val expectedUnitTestFilePath = "$RESOURCE_PATH${File.separator}$EXPECTED_UNIT_TEST_TEMPLATE_FILENAME"
        val expectedUnitTestTemplate = getTextFileContent(expectedUnitTestFilePath)

        val generator =  UnitTestGenerator()
        var unitTestTemplate = generator.generate(jiraTemplate)

        assertEquals(expectedUnitTestTemplate, unitTestTemplate)

        val jiraTemplateWithouNumerationFilePath = "$RESOURCE_PATH${File.separator}${JIRA_TEMPLATE_WITHOUT_NUMERATION_FILENAME}"
        val jiraTemplateWithoutNumeration = getTextFileContent(jiraTemplateWithouNumerationFilePath)

        unitTestTemplate = generator.generate(jiraTemplateWithoutNumeration)

        assertEquals(expectedUnitTestTemplate, unitTestTemplate)
    }

    @Test
    internal fun testWriteToFile() {
        val jiraTemplateFilePath = "$RESOURCE_PATH${File.separator}$JIRA_TEMPLATE_FILENAME"
        val jiraTemplate = getTextFileContent(jiraTemplateFilePath)

        val unitTestFilePath = "$RESOURCE_PATH${File.separator}$UNIT_TEST_TEMPLATE_FILENAME"

        val expectedUnitTestFilePath = "$RESOURCE_PATH${File.separator}$EXPECTED_UNIT_TEST_TEMPLATE_FILENAME"
        val expectedUnitTestTemplate = getTextFileContent(expectedUnitTestFilePath)

        val generator = UnitTestGenerator()
        val content = generator.generate(jiraTemplate)
        generator.writeToFile(unitTestFilePath, content)

        val actualUnitTestTemplate = getTextFileContent(unitTestFilePath)

        assertTrue(Files.exists(File(unitTestFilePath).toPath()))
        assertEquals(expectedUnitTestTemplate, actualUnitTestTemplate)
    }
}

fun getTextFileContent(path: String): String {
    return Files
            .lines(File(path).toPath())
            .collect(Collectors.joining("\n"))
}