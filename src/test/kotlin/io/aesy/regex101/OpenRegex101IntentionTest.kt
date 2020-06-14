package io.aesy.regex101

import com.intellij.openapi.application.WriteAction
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.doesNotContain
import strikt.assertions.map

class OpenRegex101IntentionTest : JUnit5CodeInsightTest() {
    companion object {
        const val actionName: String = "Open RegExp on regex101.com"
    }

    @Test
    @DisplayName("It should not display a OpenRegex101 Intention in javascript elements")
    fun testIntentionDoesNotExistsInJs() {
        myFixture.configureByFile("regex.js")

        WriteAction.runAndWait<Throwable> { myFixture.editor.caretModel.moveToOffset(1) }

        expectThat(myFixture.availableIntentions)
            .map { it.text }
            .doesNotContain(actionName)
    }

    @Test
    @DisplayName("It should display a OpenRegex101 Intention in regular expression elements")
    fun testIntentionExistsInRegex() {
        myFixture.configureByFile("regex.js")

        WriteAction.runAndWait<Throwable> { myFixture.editor.caretModel.moveToOffset(17) }

        expectThat(myFixture.availableIntentions)
            .map { it.text }
            .contains(actionName)
    }

    @Test
    @DisplayName("It should not throw when launching OpenRegex101 action")
    fun testLaunchAction() {
        myFixture.configureByFile("regex.js")

        WriteAction.runAndWait<Throwable> { myFixture.editor.caretModel.moveToOffset(17) }

        val intention = myFixture.findSingleIntention(actionName)

        myFixture.launchAction(intention)
    }
}
