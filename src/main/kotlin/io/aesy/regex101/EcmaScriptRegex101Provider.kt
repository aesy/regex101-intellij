package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class EcmaScriptRegex101Provider: Regex101Provider {
    override fun getExpression(elementInHost: PsiElement, regexp: PsiFile): String = regexp.text

    override fun getFlavor(elementInHost: PsiElement, regexp: PsiFile): String = "javascript"

    override fun getFlags(elementInHost: PsiElement, regexp: PsiFile): Set<String> {
        val modifiers = elementInHost.text.substringAfterLast('/', "")

        return modifiers.toCharArray()
            .asSequence()
            .mapNotNull {
                when (it) {
                    'i', 'm', 's', 'u' -> it.toString()
                    else -> null
                }
            }
            .toSet()
            .plus("g")
    }
}
