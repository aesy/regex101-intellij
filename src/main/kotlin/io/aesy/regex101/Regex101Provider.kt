package io.aesy.regex101

import com.intellij.lang.LanguageExtension
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

interface Regex101Provider {
    object EP: LanguageExtension<Regex101Provider>("io.aesy.regex101.regex101Provider")

    fun getExpression(element: PsiElement, file: PsiFile): String
    fun getFlavor(element: PsiElement, file: PsiFile): String
    fun getFlags(element: PsiElement, file: PsiFile): Set<String>
}
