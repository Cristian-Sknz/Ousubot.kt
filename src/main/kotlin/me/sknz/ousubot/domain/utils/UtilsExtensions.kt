package me.sknz.ousubot.domain.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import org.thymeleaf.context.IContext
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*

class TemplateContext: IContext {

    var language = Locale.getDefault()
    val variables = HashMap<String, Any>()
    lateinit var template: String

    override fun getLocale(): Locale {
        return language
    }

    override fun containsVariable(name: String?): Boolean {
        return variables.containsKey(name)
    }

    override fun getVariableNames(): MutableSet<String> {
        return variables.keys
    }

    override fun getVariable(name: String?): Any? {
        return variables[name]
    }
}

fun template(engine: SpringTemplateEngine, init: TemplateContext.() -> Unit): DiscordEmbed {
    val context = TemplateContext().apply(init)

    val mapper = XmlMapper()
        .findAndRegisterModules()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    return mapper.readValue(engine.process(context.template, context), DiscordEmbed::class.java)
}