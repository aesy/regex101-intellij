package io.aesy.regex101

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.injection.Injectable
import org.intellij.lang.regexp.RegExpFileType
import org.intellij.lang.regexp.RegExpLanguage
import org.intellij.plugins.intelliLang.inject.InjectLanguageAction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.doesNotContain
import strikt.assertions.map

class OpenRegex101IntentionTest : JUnit5CodeInsightTest() {
    companion object {
        const val actionName: String = "Open RegExp on regex101.com"
    }

    @AfterEach
    fun teardown() {
        TestUtil.openedUrls.clear()
    }

    @Test
    @DisplayName("It should not display a OpenRegex101 Intention in JavaScript elements")
    fun testIntentionDoesNotExistsInJs() {
        val file = myFixture.configureByFile("regex.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 1)

        WriteAction.runAndWait<Throwable> { myFixture.editor.caretModel.moveToOffset(offset) }

        expectThat(myFixture.availableIntentions)
            .map { it.text }
            .doesNotContain(actionName)
    }

    @Test
    @DisplayName("It should display a OpenRegex101 Intention in regular expression elements")
    fun testIntentionExistsInRegex() {
        val file = myFixture.configureByFile("regex.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 17)

        WriteAction.runAndWait<Throwable> { myFixture.editor.caretModel.moveToOffset(offset) }

        expectThat(myFixture.availableIntentions)
            .map { it.text }
            .contains(actionName)
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from JavaScript regular expression")
    fun testLaunchActionJavaScript() {
        val file = myFixture.configureByFile("regex.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 17)

        WriteAction.runAndWait<Throwable> { myFixture.editor.caretModel.moveToOffset(offset) }

        val intention = myFixture.findSingleIntention(actionName)

        myFixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .containsExactly("https://regex101.com/?regex=.%2B&flavor=javascript")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Java file")
    fun testLaunchActionJava() {
        val file = myFixture.configureByFile("pattern.java")
        val offset = StringUtil.lineColToOffset(file.text, 4, 45)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            myFixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(project, myFixture.editor, myFixture.file, injectable)
        }

        val intention = myFixture.findSingleIntention(actionName)

        myFixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .containsExactly("https://regex101.com/?regex=.%2B&flavor=pcre")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression file")
    fun testLaunchActionRegex() {
        val file = myFixture.configureByText(RegExpFileType.INSTANCE, ".+")
        val offset = StringUtil.lineColToOffset(file.text, 0, 2)

        WriteAction.runAndWait<Throwable> { myFixture.editor.caretModel.moveToOffset(offset) }

        val intention = myFixture.findSingleIntention(actionName)

        myFixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .containsExactly("https://regex101.com/?regex=.%2B&flavor=pcre")
    }
}
