package me.sknz.ousubot.infrastructure.tools

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
import okhttp3.internal.toImmutableList
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.util.*
import kotlin.io.path.isDirectory
import kotlin.io.path.toPath

/**
 * ## DiscordI18nBundle
 *
 * Objeto que contém configurações para Intercionalização
 * utilizando [ResourceBundle] e implementando [LocalizationFunction]
 * para traduzir os comandos do Discord.
 *
 * @see LocalizationFunction
 */
object DiscordI18nBundle : LocalizationFunction, ReloadableResourceBundleMessageSource() {

    val locales: List<DiscordLocale> = getDiscordLocales()

    init {
        isUseCodeAsDefaultMessage = true
        setBasename("classpath:i18n/discord")
    }

    /**
     * Função para obter uma mensagem traduzida
     * através de um operador.
     *
     * `DiscordI18nBundle["message.code", locale]`
     *
     * @see ReloadableResourceBundleMessageSource.getMessage
     */
    operator fun get(code: String, locale: Locale): String {
        return DiscordI18nBundle.getMessage(code, null, locale)
    }

    /**
     * Função para obter uma mensagem traduzida
     * através de um operador.
     *
     * `DiscordI18nBundle["message.code", discordLocale]`
     *
     * @see ReloadableResourceBundleMessageSource.getMessage
     * @see DiscordLocale
     */
    operator fun get(code: String, locale: DiscordLocale): String {
        return DiscordI18nBundle[code, Locale.forLanguageTag(locale.locale)]
    }

    /**
     * Função para obter uma mensagem traduzida
     * através de um operador.
     *
     * `DiscordI18nBundle["message.code"]`
     *
     * @see ReloadableResourceBundleMessageSource.getMessage
     */
    operator fun get(code: String): String {
        return DiscordI18nBundle[code, Locale.getDefault()]
    }

    /**
     * Aplicar a tradução em todas as [DiscordLocale]s
     * e retorna um [Map] com todas as traduções
     *
     * Pode retonar o codigo de volta caso não tenha tradução para
     * a [localizationKey].
     */
    override fun apply(localizationKey: String): MutableMap<DiscordLocale, String> {
        val map = hashMapOf<DiscordLocale, String>()
        for (locale in locales) {
            val code = "commands.$localizationKey"
            val message = getMessage(code, null, Locale.forLanguageTag(locale.locale))
            if (message != code) {
                map[locale] = message
                continue
            }
        }

        return map
    }

    /**
     * Obter todas os idiomas disponíveis nos [ResourceBundle]s
     * e transformando os compatíveis em [DiscordLocale]
     *
     * @see DiscordLocale
     */
    private fun getDiscordLocales(): List<DiscordLocale> {
        return getI18nFileNames().map {
            val valor = it.substringAfter("_")
                .split("_")

            if (valor.size == 2) {
                try {
                    return@map DiscordLocale.from(Locale(valor[0], valor[1]))
                } catch (e: IllegalArgumentException) {
                    logger.error("Idioma (${it}) não é compatível com o discord.", e)
                }
            }
            return@map null
        }.filterNotNull().toImmutableList()
    }

    /**
     * Obter todas os nomes dos arquivos de [ResourceBundle] no ClassPath
     */
    private fun getI18nFileNames(): List<String> {
        val path = ClassPathResource("i18n", this.javaClass.classLoader).uri.toPath()
        return Files.newDirectoryStream(path).map {
            if (it.isDirectory()) {
                return@map null
            }
            val name = it.toFile().nameWithoutExtension
            if (!name.contains("_")) {
                return@map null
            }
            return@map name
        }.filterNotNull()
    }

    fun CommandInteraction.reply(code: String, locale: DiscordLocale): ReplyCallbackAction {
        return this.reply(DiscordI18nBundle[code, locale])
    }
}