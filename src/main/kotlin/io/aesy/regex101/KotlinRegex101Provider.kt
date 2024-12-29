package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.*

class KotlinRegex101Provider: Regex101Provider {
    override fun getExpression(elementInHost: PsiElement, regexp: PsiFile): String = regexp.text

    override fun getFlavor(elementInHost: PsiElement, regexp: PsiFile): String = "java"

    override fun getFlags(elementInHost: PsiElement, regexp: PsiFile): Set<String> {
        if (elementInHost !is KtExpression) {
            return emptySet()
        }

        val list = PsiTreeUtil.getParentOfType(elementInHost, KtValueArgumentList::class.java)
            ?: return emptySet()
        val arguments = list.arguments

        if (arguments.size != 2) {
            return emptySet()
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
            .mapNotNull {
                when (it) {
                    "IGNORE_CASE" -> "i"
                    "MULTILINE" -> "m"
                    "DOT_MATCHES_ALL" -> "s"
                    "UNIX_LINES" -> "d"
                    else -> null
                }
            }
            .toSet()
            .plus("g")
    }
}
