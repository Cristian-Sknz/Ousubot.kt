package me.sknz.ousubot.infrastructure.xml.processor

import me.sknz.ousubot.infrastructure.tools.CustomEmojis
import me.sknz.ousubot.infrastructure.tools.CustomEmojis.xml
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

class EmojiProcessor(
    prefix: String,
) : AbstractAttributeTagProcessor(
    TemplateMode.XML,
    prefix,
    null,
    false,
    "emoji",
    true,
    1000,
    true
) {
    override fun doProcess(
        context: ITemplateContext,
        tag: IProcessableElementTag,
        attributeName: AttributeName,
        attributeValue: String,
        structureHandler: IElementTagStructureHandler
    ) {
        val emoji = CustomEmojis[getEmoteName(context, attributeValue)]?.xml()
        val text = context.modelFactory.createText(emoji ?: "emote not found")
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
}