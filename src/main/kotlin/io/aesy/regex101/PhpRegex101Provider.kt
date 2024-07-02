package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class PhpRegex101Provider: Regex101Provider {
    override fun getExpression(element: PsiElement, file: PsiFile): String = element.text

    override fun getFlavor(element: PsiElement, file: PsiFile): String = "pcre2"

    override fun getFlags(element: PsiElement, file: PsiFile): Set<String> = setOf("g")
}
