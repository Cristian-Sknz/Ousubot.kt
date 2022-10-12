package me.sknz.ousubot.domain.services.impl

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import me.skiincraft.ousucanvas.elements.ElementAlignment
import me.skiincraft.ousucanvas.image.ImageElement
import me.skiincraft.ousucanvas.text.TextOrientation
import me.sknz.ousubot.app.api.OsuClientAPI
import me.sknz.ousubot.app.api.models.users.User
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import me.sknz.ousubot.domain.dto.DiscordUserEmbed
import me.sknz.ousubot.domain.dto.UserRequest
import me.sknz.ousubot.domain.services.UserService
import me.sknz.ousubot.infrastructure.tools.KotlinImageBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import javax.imageio.ImageIO

@Service
class UserServiceImpl(override val client: OsuClientAPI,
                      override val engine: SpringTemplateEngine) : UserService<UserServiceImpl> {

    @Autowired
    @Lazy
    override lateinit var self: UserServiceImpl

    override fun getUserService(request: UserRequest): DiscordUserEmbed {
        val user = client.getUser(request.username)
        val embed = process(user, request.locale)

        return DiscordUserEmbed(embed, DiscordUserEmbed.DiscordUserPayload(user, generateImage(user)))
    }

    fun generateImage(user: User): BufferedImage {
        return KotlinImageBuilder(900, 250, BufferedImage.TYPE_INT_RGB).dsl {
            val position = intArrayOf(150, 294, 444, 594, 739).iterator()
            val layer = ImageIO.read(ClassPathResource("assets/layer.png", this.javaClass.classLoader).inputStream)

            background = ImageElement(user.coverUrl)

            element {
                element = ImageElement(layer)
                alignment = ElementAlignment.BOTTOM_LEFT
            }

            for (note in user.statistics!!.grade.values()) {
                element {
                    text {
                        text = "$note"
                        font = font.deriveFont(34F)
                        orientation = TextOrientation.MIDDLE
                        color = Color.BLACK
                    }
                    x = position.next()
                    y = 211
                    alignment =  ElementAlignment.CENTER
                }
            }
        }
    }

    fun process(user: User, locale: Locale): DiscordEmbed {
        val ctx = Context()
            .addVariable("user", user)
            .addVariable("color", (user.profileColour?.let {
                Color.decode(it)
            } ?: Color.ORANGE).rgb)

        ctx.locale = locale

        val xml = engine.process("UserEmbed", ctx)

        val mapper = XmlMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return mapper.readValue(xml, DiscordEmbed::class.java)
    }

    private fun Context.addVariable(key: String, value: Any): Context {
        this.setVariable(key, value)
        return this
    }
}