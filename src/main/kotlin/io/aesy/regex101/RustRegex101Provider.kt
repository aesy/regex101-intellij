package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class RustRegex101Provider: Regex101Provider {
    override fun getExpression(element: PsiElement, file: PsiFile): String = element.text

    override fun getFlavor(element: PsiElement, file: PsiFile): String = "rust"

    override fun getFlags(element: PsiElement, file: PsiFile): Set<String> = setOf("g")
}
