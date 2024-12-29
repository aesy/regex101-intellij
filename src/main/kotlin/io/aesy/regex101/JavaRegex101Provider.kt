package io.aesy.regex101

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil

class JavaRegex101Provider: Regex101Provider {
    override fun getExpression(elementInHost: PsiElement, regexp: PsiFile): String = regexp.text

    override fun getFlavor(elementInHost: PsiElement, regexp: PsiFile): String = "java"

    override fun getFlags(elementInHost: PsiElement, regexp: PsiFile): Set<String> {
        if (elementInHost !is PsiExpression) {
            return emptySet()
        }

        val list = PsiTreeUtil.getParentOfType(elementInHost, PsiExpressionList::class.java)
            ?: return emptySet()
        val expressions = list.expressions

        if (expressions.size != 2) {
            return emptySet()
        }

        val flags = expressions[1].text

        return flags.split("|")
            .asSequence()
            .map(String::trim)
            .map { it.removePrefix("java.util.regex.") }
            .map { it.removePrefix("Pattern.") }
            .mapNotNull {
                when (it) {
                    "CASE_INSENSITIVE" -> "i"
                    "MULTILINE" -> "m"
                    "DOTALL" -> "s"
                    "UNICODE_CASE" -> "u"
                    "UNICODE_CHARACTER_CLASS" -> "U"
                    "UNIX_LINES" -> "d"
                    else -> null
                }
            }
            .toSet()
            .plus("g")
    }
}
