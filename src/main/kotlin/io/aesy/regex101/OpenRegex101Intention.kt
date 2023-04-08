package io.aesy.regex101

import com.intellij.codeInsight.intention.impl.QuickEditAction
import com.intellij.ide.BrowserUtil
import com.intellij.lang.Language
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.intellij.lang.regexp.RegExpLanguage
import org.intellij.lang.regexp.RegExpModifierProvider
import java.net.URLEncoder
import java.util.regex.Pattern
import javax.swing.Icon

class OpenRegex101Intention: QuickEditAction(), Iconable {
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
        val regex = text.urlEncode()
        val flavor = element.language.toFlavor()
        val flags = element.getFlags()

        val url = "$domain/?regex=$regex&flavor=$flavor&flags=$flags"

        if (ApplicationManager.getApplication().isUnitTestMode) {
            TestUtil.openedUrls += url
        } else {
            BrowserUtil.browse(url)
        }
    }

    private fun PsiElement.getFlags(): String {
        val injectedLanguageManager = InjectedLanguageManager.getInstance(project)
        val host = injectedLanguageManager.getInjectionHost(this)
        var flags = 0

        if (host != null) {
            for (provider in RegExpModifierProvider.EP.allForLanguage(host.language)) {
                flags = flags or provider.getFlags(host, containingFile)
            }
        }

        var chars = "g"

        if (flags.hasFlag(Pattern.MULTILINE)) {
            chars += 'm'
        }

        if (flags.hasFlag(Pattern.DOTALL)) {
            chars += 's'
        }

        if (flags.hasFlag(Pattern.CASE_INSENSITIVE)) {
            chars += 'i'
        }

        if (flags.hasFlag(Pattern.UNICODE_CASE) || flags.hasFlag(Pattern.UNICODE_CHARACTER_CLASS)) {
            chars += 'u'
        }

        return chars
    }

    private fun Int.hasFlag(flag: Int): Boolean = flag and this != 0

    private fun getElement(file: PsiFile, editor: Editor): PsiElement {
        return getRangePair(file, editor)?.first ?: file
    }

    private fun String.urlEncode(): String = URLEncoder.encode(this, Charsets.UTF_8.name())

    private fun Language.toFlavor(): String = when (id) {
        "JSRegexp", "JSUnicodeRegexp" -> "javascript"
        "PythonRegExp", "PythonVerboseRegExp" -> "python"
        "GoRegExp" -> "golang"
        else -> "pcre"
    }
}
