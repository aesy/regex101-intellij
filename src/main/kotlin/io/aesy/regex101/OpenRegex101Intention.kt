package io.aesy.regex101

import com.intellij.codeInsight.intention.impl.QuickEditAction
import com.intellij.ide.BrowserUtil
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.lang.Language
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.intellij.lang.regexp.RegExpLanguage
import java.net.URLEncoder
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

        val injectedLanguageManager = InjectedLanguageManager.getInstance(project)
        val text = injectedLanguageManager.getUnescapedText(element)
        val flavor = element.language.toFlavor()
        val regex = urlEncode(text)
        val url = "$domain/?regex=$regex&flavor=$flavor"

        if (PluginManagerCore.isUnitTestMode) {
            TestUtil.openedUrls += url
        } else {
            BrowserUtil.browse(url)
        }
    }

    private fun getElement(file: PsiFile, editor: Editor): PsiElement {
        return getRangePair(file, editor)?.first ?: file
    }

    private fun urlEncode(text: String): String = URLEncoder.encode(text, Charsets.UTF_8.name())

    private fun Language.toFlavor(): String = when (id) {
        "JSRegexp", "JSUnicodeRegexp" -> "javascript"
        "PythonRegExp", "PythonVerboseRegExp" -> "python"
        "GoRegExp" -> "golang"
        else -> "pcre"
    }
}
