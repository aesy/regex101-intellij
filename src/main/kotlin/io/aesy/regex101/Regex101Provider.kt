package io.aesy.regex101

import com.intellij.lang.LanguageExtension
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

interface Regex101Provider {
    object EP: LanguageExtension<Regex101Provider>("io.aesy.regex101.regex101Provider")

    fun getExpression(elementInHost: PsiElement, regexp: PsiFile): String
    fun getFlavor(elementInHost: PsiElement, regexp: PsiFile): String
    fun getFlags(elementInHost: PsiElement, regexp: PsiFile): Set<String>
}
