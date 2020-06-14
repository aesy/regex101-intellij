package io.aesy.regex101

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class JUnit5CodeInsightTest : LightJavaCodeInsightFixtureTestCase() {
    override fun getTestDataPath(): String {
        val resourceDirectory = JUnit5CodeInsightTest::class.java.getResource("/testData").toURI()

        return Paths.get(resourceDirectory).toString()
    }

    @BeforeEach
    override fun setUp() {
        super.setUp()
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
    }
}
