package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.intellij.lang.regexp.RegExpModifierProvider
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.kotlin.psi.KtValueArgumentList
import java.lang.IllegalArgumentException
import java.util.regex.Pattern

class KotlinRegExpModifierProvider : RegExpModifierProvider {
    override fun getFlags(element: PsiElement, file: PsiFile): Int {
        if (element !is KtExpression) {
            return 0
        }

        val list = PsiTreeUtil.getParentOfType(element, KtValueArgumentList::class.java)
            ?: return 0
        val arguments = list.arguments

        if (arguments.size != 2) {
            return 0
        }

        val options = PsiTreeUtil.findChildrenOfType(arguments[1], KtValueArgument::class.java)

        if (options.isEmpty()) {
            options += arguments[1]
        }

        return options
            .asSequence()
            .map(KtValueArgument::getText)
            .map { it.removePrefix("kotlin.text.") }
            .map { it.removePrefix("RegexOption.") }
            .map(this::parsePatternOption)
            .reduce(Int::or)
    }

    private fun parsePatternOption(input: String): Int {
        val option = try {
            RegexOption.valueOf(input)
        } catch (e: IllegalArgumentException) {
            return 0
        }

        return when (option) {
            RegexOption.IGNORE_CASE -> Pattern.CASE_INSENSITIVE
            RegexOption.MULTILINE -> Pattern.MULTILINE
            RegexOption.LITERAL -> Pattern.LITERAL
            RegexOption.UNIX_LINES -> Pattern.UNIX_LINES
            RegexOption.COMMENTS -> Pattern.COMMENTS
            RegexOption.DOT_MATCHES_ALL -> Pattern.DOTALL
            RegexOption.CANON_EQ -> Pattern.CANON_EQ
        }
    }
}
