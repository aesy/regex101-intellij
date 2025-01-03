package io.aesy.regex101

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.injection.Injectable
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.intellij.lang.regexp.RegExpFileType
import org.intellij.lang.regexp.RegExpLanguage
import org.intellij.plugins.intelliLang.inject.InjectLanguageAction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
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
        const val ACTION_NAME: String = "Open RegExp on regex101.com"
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
        val file = fixture.configureByFile("regexp.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 1)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        expectThat(fixture.availableIntentions)
            .map { it.text }
            .doesNotContain(ACTION_NAME)
    }

    @Test
    @DisplayName("It should display a OpenRegex101 Intention in regular expression elements")
    fun testIntentionExistsInRegex() {
        val file = fixture.configureByFile("regexp.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 17)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        expectThat(fixture.availableIntentions)
            .map { it.text }
            .contains(ACTION_NAME)
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from JavaScript regular expression")
    fun testLaunchActionJavaScript() {
        val file = fixture.configureByFile("regexp.js")
        val offset = StringUtil.lineColToOffset(file.text, 0, 17)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=javascript&flags=img")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from TypeScript regular expression")
    fun testLaunchActionTypeScript() {
        val file = fixture.configureByFile("regexp.ts")
        val offset = StringUtil.lineColToOffset(file.text, 0, 25)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=javascript&flags=img")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Java file")
    fun testLaunchActionJava() {
        val file = fixture.configureByFile("Pattern.java")
        val offset = StringUtil.lineColToOffset(file.text, 6, 44)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            fixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(fixture.project, fixture.editor, file, injectable)
        }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=java&flags=img")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Kotlin file")
    fun testLaunchActionKotlin() {
        val file = fixture.configureByFile("Regex.kt")
        val offset = StringUtil.lineColToOffset(file.text, 2, 22)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            fixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(fixture.project, fixture.editor, file, injectable)
        }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=java&flags=img")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Go file")
    fun testLaunchActionGo() {
        val file = fixture.configureByFile("regex.go")
        val offset = StringUtil.lineColToOffset(file.text, 7, 29)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            fixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(fixture.project, fixture.editor, file, injectable)
        }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=golang&flags=g")
    }

    @Disabled
    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Php file")
    fun testLaunchActionPhp() {
        val file = fixture.configureByFile("regex.php")
        val offset = StringUtil.lineColToOffset(file.text, 0, 18)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            fixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(fixture.project, fixture.editor, file, injectable)
        }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=pcre2&flags=g")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Python file")
    fun testLaunchActionPython() {
        val file = fixture.configureByFile("regex.py")
        val offset = StringUtil.lineColToOffset(file.text, 2, 18)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            fixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(fixture.project, fixture.editor, file, injectable)
        }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=python&flags=g")
    }

    // https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/1819
    @Disabled
    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression injected in Rust file")
    fun testLaunchActionRust() {
        val file = fixture.configureByFile("Regex.rs")
        val offset = StringUtil.lineColToOffset(file.text, 3, 28)
        val injectable = Injectable.fromLanguage(RegExpLanguage.INSTANCE)

        WriteAction.runAndWait<Throwable> {
            fixture.editor.caretModel.moveToOffset(offset)
            InjectLanguageAction.invokeImpl(fixture.project, fixture.editor, file, injectable)
        }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=rust&flags=g")
    }

    @Test
    @DisplayName("It should be able to launch OpenRegex101 action from regular expression file")
    fun testLaunchActionRegex() {
        val file = fixture.configureByText(RegExpFileType.INSTANCE, "[abc]")
        val offset = StringUtil.lineColToOffset(file.text, 0, 2)

        WriteAction.runAndWait<Throwable> { fixture.editor.caretModel.moveToOffset(offset) }

        val intention = fixture.findSingleIntention(ACTION_NAME)

        fixture.launchAction(intention)

        expectThat(TestUtil.openedUrls)
            .describedAs("Opened URLs")
            .containsExactly("https://regex101.com/?regex=%5Babc%5D&flavor=pcre2&flags=g")
    }
}
