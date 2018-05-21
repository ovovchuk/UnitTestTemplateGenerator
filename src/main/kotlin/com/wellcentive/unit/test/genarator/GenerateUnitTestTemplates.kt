package com.wellcentive.unit.test.genarator

import com.wellcentive.unit.test.genarator.util.Constants.JIRA_TEMPLATE_FILENAME
import com.wellcentive.unit.test.genarator.util.Constants.RESOURCES_PATH
import com.wellcentive.unit.test.genarator.util.Constants.SEPARATOR
import com.wellcentive.unit.test.genarator.util.Constants.UNIT_TEST_TEMPLATE_FILENAME
import java.io.File
import java.nio.file.Files
import java.util.stream.Collectors

fun main(args: Array<String>) {
    val jiraTemplate = getTextFileContent(
            "$RESOURCES_PATH$SEPARATOR$JIRA_TEMPLATE_FILENAME")

    val generator = UnitTestGenerator()
    val content = generator.generate(jiraTemplate)
    generator.writeToFile(
            "$RESOURCES_PATH$SEPARATOR$UNIT_TEST_TEMPLATE_FILENAME",
            content
    )
}

fun getTextFileContent(path: String): String {
    return Files
            .lines(File(path).toPath())
            .collect(Collectors.joining("\n"))
}