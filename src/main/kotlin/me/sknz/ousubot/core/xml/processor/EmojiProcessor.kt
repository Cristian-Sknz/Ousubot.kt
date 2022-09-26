package me.sknz.ousubot.core.xml.processor

import com.fasterxml.jackson.databind.ObjectMapper
import net.dv8tion.jda.api.entities.emoji.CustomEmoji
import net.dv8tion.jda.api.entities.emoji.Emoji
import org.springframework.core.io.ClassPathResource
import org.thymeleaf.IEngineConfiguration
import org.thymeleaf.context.ITemplateContext
import org.thymeleaf.engine.AttributeName
import org.thymeleaf.model.IProcessableElementTag
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor
import org.thymeleaf.processor.element.IElementTagStructureHandler
import org.thymeleaf.standard.expression.IStandardExpression
import org.thymeleaf.standard.expression.IStandardExpressionParser
import org.thymeleaf.standard.expression.StandardExpressions
import org.thymeleaf.templatemode.TemplateMode

class EmojiProcessor(prefix: String) : AbstractAttributeTagProcessor(
    TemplateMode.XML,
    prefix,
    null,
    false,
    "emoji",
    true,
    1000,
    true
) {

    private val emoji: ArrayList<CustomEmoji> = arrayListOf()

    init {
        val path = ClassPathResource("assets/emoji.json", this.javaClass.classLoader)
        if (path.exists()) {
            val json = ObjectMapper().readTree(path.inputStream)
            for (node in json) {
                emoji.add(Emoji.fromCustom(node.get("name").asText(), node.get("id").asLong(), false))
            }
        }
    }

    override fun doProcess(
        context: ITemplateContext,
        tag: IProcessableElementTag,
        attributeName: AttributeName,
        attributeValue: String,
        structureHandler: IElementTagStructureHandler
    ) {
        val value = getEmoteName(context, attributeValue)
        val emoji = value?.let { name ->
            emoji.find { it.name.equals(name, true) }
        }
        val text = context.modelFactory.createText(emoji?.asXMLString() ?: "emote not found")
        structureHandler.replaceWith(text, false)
    }

    fun getEmoteName(context: ITemplateContext, name: String): String? {
        if (!name.startsWith("\${")) {
            return name
        }

        val configuration: IEngineConfiguration = context.configuration
        val parser: IStandardExpressionParser = StandardExpressions.getExpressionParser(configuration)
        val expression: IStandardExpression = parser.parseExpression(context, name)

        val value = expression.execute(context)
        if (value is String) {
            return value
        }
        if (value != null) {
            return value.toString()
        }
        return null
    }

    private fun CustomEmoji.asXMLString() = "&lt;:${this.name}:${this.id}&gt;"
}