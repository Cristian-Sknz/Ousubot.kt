package me.sknz.ousubot.spring

import me.sknz.ousubot.core.context.CustomEmojis
import me.sknz.ousubot.core.xml.DiscordDialect
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templatemode.TemplateMode

/**
 * ## TemplateEngineConfiguration
 *
 * Classe de configuração do Spring com o thymeleaf
 * para arquivos XML.
 *
 * Estes arquivos XMLs serão utilizados para mandar mensagens
 * prontas para o Discord como [net.dv8tion.jda.api.entities.MessageEmbed]
 *
 * @see me.sknz.ousubot.core.xml.DiscordEmbed
 * @see net.dv8tion.jda.api.entities.MessageEmbed
 */
@Configuration
class TemplateEngineConfiguration {

    @Bean
    fun springResourceTemplateResolver(): SpringResourceTemplateResolver {
        val resolver = SpringResourceTemplateResolver()
        resolver.setApplicationContext(AnnotationConfigApplicationContext())
        resolver.prefix = "classpath:/embeds/"
        resolver.suffix = ".xml"
        resolver.characterEncoding = "UTF-8"
        resolver.templateMode = TemplateMode.XML

        return resolver
    }

    @Bean
    fun customEmojis(): CustomEmojis {
        return CustomEmojis()
    }

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setUseCodeAsDefaultMessage(true)
        messageSource.setBasename("classpath:i18n/discord")
        return messageSource
    }

    @Bean
    fun springTemplateEngine(@Qualifier("springResourceTemplateResolver") resolver: SpringResourceTemplateResolver,
                             messageSource: ReloadableResourceBundleMessageSource): SpringTemplateEngine {
        val engine = SpringTemplateEngine()
        engine.setTemplateResolver(resolver)
        engine.addDialect(Java8TimeDialect())
        engine.addDialect(DiscordDialect(customEmojis()))
        engine.setTemplateEngineMessageSource(messageSource)

        return engine
    }
}