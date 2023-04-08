package io.aesy.regex101

import com.intellij.codeInsight.JavaRegExpModifierProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiExpressionList
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.intellij.lang.regexp.RegExpModifierProvider
import java.util.regex.Pattern

/**
 * The standard modifier provider for Java is JavaRegExpModifierProvider. A downside of using the default provider is
 * that it will only resolve flags given as "constants", of which final fields are not included... This class is a bit
 * of a hack in order to be able to provide flags given as references to Pattern flags.
 *
 * @see JavaRegExpModifierProvider
 */
class JavaPatternRegExpModifierProvider: RegExpModifierProvider {
    override fun getFlags(element: PsiElement, file: PsiFile): Int {
        if (element !is PsiExpression) {
            return 0
        }

        val list = PsiTreeUtil.getParentOfType(element, PsiExpressionList::class.java)
            ?: return 0
        val expressions = list.expressions

        if (expressions.size != 2) {
            return 0
        }

        val flags = expressions[1].text

        return flags.split('|')
            .asSequence()
            .map(String::trim)
            .map { it.removePrefix("java.util.regex.") }
            .map { it.removePrefix("Pattern.") }
            .map(this::parsePatternFlag)
            .reduce(Int::or)
    }

    private fun parsePatternFlag(input: String): Int {
        return when (input) {
            "CASE_INSENSITIVE" -> Pattern.CASE_INSENSITIVE
            "MULTILINE" -> Pattern.MULTILINE
            "DOTALL" -> Pattern.DOTALL
            "LITERAL" -> Pattern.LITERAL
            "CANON_EQ" -> Pattern.CANON_EQ
            "COMMENTS" -> Pattern.COMMENTS
            "UNICODE_CASE" -> Pattern.UNICODE_CASE
            "UNICODE_CHARACTER_CLASS" -> Pattern.UNICODE_CHARACTER_CLASS
            "UNIX_LINES" -> Pattern.UNIX_LINES
            else -> 0
        }
    }
}
