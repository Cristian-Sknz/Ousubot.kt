package me.sknz.ousubot.infrastructure.tools

import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.io.ClassPathResource
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.util.*
import kotlin.io.path.isDirectory
import kotlin.io.path.toPath

object DiscordI18nBundle : LocalizationFunction, ReloadableResourceBundleMessageSource() {

    private val locales: List<DiscordLocale> = getDiscordLocales()

    init {
        isUseCodeAsDefaultMessage = true
        setBasename("classpath:i18n/discord")
    }

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
        }.filterNotNull()
    }

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
}