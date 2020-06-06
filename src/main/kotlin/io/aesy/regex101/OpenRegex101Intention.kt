package io.aesy.regex101

import com.intellij.codeInsight.intention.impl.QuickEditAction
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.intellij.lang.regexp.RegExpLanguage
import javax.swing.Icon

class OpenRegex101Intention : QuickEditAction(), Iconable {
    companion object {
        const val domain: String = "https://regex101.com"
    }

    override fun getText(): String = "Open RegExp on regex101.com"

    override fun getFamilyName(): String = text

    override fun getIcon(flags: Int): Icon? = RegExpLanguage.INSTANCE.associatedFileType?.icon

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return getElement(file, editor).language.isKindOf(RegExpLanguage.INSTANCE)
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val element = getElement(file, editor)

        if (!element.language.isKindOf(RegExpLanguage.INSTANCE)) {
            return
        }

        val regex = StringUtil.unescapeBackSlashes(element.text)
        val url = "$domain/?regex=$regex&flavor=pcre"

        BrowserUtil.browse(url)
    }

    private fun getElement(file: PsiFile, editor: Editor): PsiElement {
        return getRangePair(file, editor)?.first ?: file
    }
}
