package me.sknz.ousubot.services

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.dto.BeatmapDiscordEmbed
import me.sknz.ousubot.dto.BeatmapRequest
import me.sknz.ousubot.utils.ColorThief
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine

@Service
class BeatmapService(
    private var client: OsuClientAPI,
    private var engine: SpringTemplateEngine
) {

    @Cacheable(cacheNames = ["beatmaps"], key = "#request")
    fun getBeatmapEmbed(request: BeatmapRequest): BeatmapDiscordEmbed {
        val beatmap = client.getBeatmap(request.id)

        val ctx = Context()
        ctx.setVariable("beatmap", beatmap)
        ctx.setVariable("color", ColorThief.getPredominatColor(beatmap.beatmapSet!!.covers.card, true).rgb)
        val xml = engine.process("BeatmapInfo", ctx)

        val mapper = XmlMapper()
            .findAndRegisterModules()

        val embed = mapper.readValue(xml, BeatmapDiscordEmbed::class.java)
        embed.beatmap = beatmap

        return embed
    }
}