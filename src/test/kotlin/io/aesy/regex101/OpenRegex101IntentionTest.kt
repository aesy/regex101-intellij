package io.aesy.regex101

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.injection.Injectable
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.intellij.lang.regexp.RegExpFileType
import org.intellij.lang.regexp.RegExpLanguage
import org.intellij.plugins.intelliLang.inject.InjectLanguageAction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.doesNotContain
import strikt.assertions.map

@ExtendWith(IntelliJExtension::class)
class OpenRegex101IntentionTest {
    companion object {
        const val actionName: String = "Open RegExp on regex101.com"
    }

    @IntelliJ
    private lateinit var fixture: CodeInsightTestFixture

    @AfterEach
    fun teardown() {
        TestUtil.openedUrls.clear()
    }

    @Test
    @DisplayName("It should not display a OpenRegex101 Intention in JavaScript elements")
    fun testIntentionDoesNotExistsInJs() {
        val file = fixture.configureByFile("regex.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 1)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        expectThat(fixture.availableIntentions)
            .map { it.text }
            .doesNotContain(actionName)
    }

    @Test
    @DisplayName("It should display a OpenRegex101 Intention in regular expression elements")
    fun testIntentionExistsInRegex() {
        val file = fixture.configureByFile("regex.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 17)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        expectThat(fixture.availableIntentions)
            .map { it.text }
            .contains(actionName)
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from JavaScript regular expression")
    fun testLaunchActionJavaScript() {
        val file = fixture.configureByFile("regex.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 17)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        val intention = fixture.findSingleIntention(actionName)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .containsExactly("https://regex101.com/?regex=.%2B&flavor=javascript")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Java file")
    fun testLaunchActionJava() {
        val file = fixture.configureByFile("pattern.java")
        val offset = StringUtil.lineColToOffset(file.text, 4, 45)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            fixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(project, fixture.editor, fixture.file, injectable)
        }

        val intention = fixture.findSingleIntention(actionName)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .containsExactly("https://regex101.com/?regex=.%2B&flavor=pcre")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression file")
    fun testLaunchActionRegex() {
        val file = fixture.configureByText(RegExpFileType.INSTANCE, ".+")
        val offset = StringUtil.lineColToOffset(file.text, 0, 2)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        val intention = fixture.findSingleIntention(actionName)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .containsExactly("https://regex101.com/?regex=.%2B&flavor=pcre")
    }
}
