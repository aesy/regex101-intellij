package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class PythonRegex101Provider: Regex101Provider {
    override fun getExpression(elementInHost: PsiElement, regexp: PsiFile): String = regexp.text

    override fun getFlavor(elementInHost: PsiElement, regexp: PsiFile): String = "python"

    override fun getFlags(elementInHost: PsiElement, regexp: PsiFile): Set<String> = setOf("g")
}
