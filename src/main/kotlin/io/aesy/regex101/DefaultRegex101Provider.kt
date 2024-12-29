package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class DefaultRegex101Provider: Regex101Provider {
    companion object {
        val INSTANCE: DefaultRegex101Provider = DefaultRegex101Provider()
    }

    override fun getExpression(elementInHost: PsiElement, regexp: PsiFile): String = regexp.text

    override fun getFlavor(elementInHost: PsiElement, regexp: PsiFile): String = "pcre2"

    override fun getFlags(elementInHost: PsiElement, regexp: PsiFile): Set<String> = setOf("g")
}
