package io.aesy.regex101

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class EcmaScriptRegex101Provider: Regex101Provider {
    override fun getExpression(element: PsiElement, file: PsiFile): String {
        return element.text.substringBeforeLast('/', "")
    }

    override fun getFlavor(element: PsiElement, file: PsiFile): String = "javascript"

    override fun getFlags(element: PsiElement, file: PsiFile): Set<String> {
        val modifiers = element.text.substringAfterLast('/', "")

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
