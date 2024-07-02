package io.aesy.regex101

import com.intellij.codeInsight.intention.impl.QuickEditAction
import com.intellij.ide.BrowserUtil
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.intellij.lang.regexp.RegExpLanguage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.swing.Icon

class OpenRegex101Intention: QuickEditAction(), Iconable {
    companion object {
        const val DOMAIN: String = "https://regex101.com"
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

        val url = getUrl(project, element, file) ?: return

        if (ApplicationManager.getApplication().isUnitTestMode) {
            TestUtil.openedUrls += url
        } else {
            BrowserUtil.browse(url)
        }
    }

    private fun getUrl(project: Project, element: PsiElement, file: PsiFile): String? {
        val provider = getProvider(project, element, file)
        val regex = provider.getExpression(element, file).urlEncode()
        val flavor = provider.getFlavor(element, file)
        val flags = provider.getFlags(element, file).joinToString()

        return "$DOMAIN/?regex=$regex&flavor=$flavor&flags=$flags"
    }

    private fun getProvider(project: Project, element: PsiElement, file: PsiFile): Regex101Provider {
        val injectedLanguageManager = InjectedLanguageManager.getInstance(project)
        var host = injectedLanguageManager.getInjectionHost(element)

        if (host != null) {
            val provider = Regex101Provider.EP.forLanguage(host.language)

            if (provider != null) {
                return provider
            }
        }

        host = injectedLanguageManager.getInjectionHost(file)

        if (host != null) {
            val provider = Regex101Provider.EP.forLanguage(host.language)

            if (provider != null) {
                return provider
            }
        }

        return DefaultRegex101Provider.INSTANCE
    }

    private fun getElement(file: PsiFile, editor: Editor): PsiElement {
        return getRangePair(file, editor)?.first ?: file
    }

    private fun String.urlEncode(): String = URLEncoder.encode(this, StandardCharsets.UTF_8)
}
