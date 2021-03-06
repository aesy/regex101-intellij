package io.aesy.regex101

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.ecma6.TypeScriptLiteralType
import com.intellij.lang.javascript.regexp.JSRegExpModifierProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.intellij.lang.regexp.RegExpModifierProvider
import java.util.regex.Pattern

/**
 * Substitute for JSRegExpModifierProvider which, unlike JSRegExpModifierProvider, works with TypeScript and
 * supports more modifiers.
 *
 * @see JSRegExpModifierProvider
 */
class EcmaScriptRegExpModifierProvider : RegExpModifierProvider {
    override fun getFlags(element: PsiElement, file: PsiFile): Int {
        val text = when (element) {
            is JSLiteralExpression -> element.text
            is TypeScriptLiteralType -> element.text
            else -> return 0
        }
        val modifiers = text.substringAfterLast('/', "")

        return modifiers.toCharArray()
            .asSequence()
            .map(this::parseRegExpModifier)
            .reduce(Int::or)
    }

    private fun parseRegExpModifier(input: Char): Int {
        return when (input) {
            'i' -> Pattern.CASE_INSENSITIVE
            'm' -> Pattern.MULTILINE
            's' -> Pattern.DOTALL
            'u' -> Pattern.UNICODE_CHARACTER_CLASS or Pattern.UNICODE_CASE
            else -> 0
        }
    }
}
