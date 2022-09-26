package me.sknz.ousubot.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.api.models.beatmaps.Beatmap
import me.sknz.ousubot.api.models.beatmaps.BeatmapSet
import me.sknz.ousubot.dto.BeatmapDiscordEmbed
import me.sknz.ousubot.dto.BeatmapRequest
import me.sknz.ousubot.dto.BeatmapSetDiscordEmbed
import me.sknz.ousubot.utils.ColorThief
import net.dv8tion.jda.api.interactions.DiscordLocale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine

/**
 * ## BeatmapService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands
 *
 * @see me.sknz.ousubot.commands.BeatmapController
 */
@Service
class BeatmapService(
    private val client: OsuClientAPI,
    private val engine: SpringTemplateEngine
) {

    /**
     * Pegar a instância desta classe como proxy.
     *
     * A anotação [Cacheable] funciona apenas na instância com proxy,
     * se for utilizado o `this` o caching não irã funcionar.
     */
    @Autowired
    @Lazy
    private lateinit var self: BeatmapService

    /**
     * Função para pegar/criar uma mensagem pronta para um Beatmap.
     *
     * Esta função contém armazenamento em cachê para evitar chamadas excessivas a
     * API do Osu e também evitar processamento desnecessário de recursos.
     *
     * @param request Requisição de Beatmap
     * @param map Gerar a partir de um [Beatmap]
     */
    @Cacheable(cacheNames = ["beatmaps"], key = "#request")
    fun getBeatmapEmbed(request: BeatmapRequest, map: Beatmap? = null): BeatmapDiscordEmbed {
        val beatmap = map ?: client.getBeatmap(request.id)
        val ctx = Context()
            .addVariable("beatmap", beatmap)
            .addVariable("color", ColorThief.getPredominatColor(beatmap.beatmapSet!!.covers.card, true).rgb)

        val xml = engine.process("BeatmapInfo", ctx)

        val mapper = XmlMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val embed = mapper.readValue(xml, BeatmapDiscordEmbed::class.java)
        embed.beatmap = beatmap

        return embed
    }

    /**
     * Função para pegar um BeatmapSet.
     *
     * Esta função contém armazenamento em cachê para evitar chamadas excessivas a
     * API do Osu e também evitar processamento desnecessário de recursos.
     *
     * @param id BeatmapSetId
     */
    @Cacheable(cacheNames=["beatmapSets"], key = "#id")
    fun getBeatmapSet(id: Int): BeatmapSet {
        return client.getBeatmapSet(id)
    }

    /**
     * Função para pegar/criar uma mensagem pronta para um Beatmap.
     *
     * Apesar de não ter a anotação [Cacheable] declarada, esta função assim como [BeatmapService.getBeatmapEmbed]
     * também utiliza de armazenamento em cachê.
     *
     * @see BeatmapService.getBeatmapEmbed
     *
     * @param beatmapSetId Id do BeatmapSet
     * @param locale Locale utilizado para intercionalização (i18n)
     * @param beatmapId devolver um BeatmapId especifico
     */
    fun getBeatmapSetEmbed(beatmapSetId: Int, locale: DiscordLocale, beatmapId: Int? = null): BeatmapSetDiscordEmbed {
        val beatmapSet = self.getBeatmapSet(beatmapSetId)
        val beatmaps = beatmapSet.beatmaps!!.sortedBy { it.difficultyRating }

        if (beatmapId == null) {
            val beatmap = beatmaps[0]
            beatmap.beatmapSet = beatmapSet.cloneWithoutBeatmaps()
            val embed = self.getBeatmapEmbed(BeatmapRequest(beatmap.id, locale))

            return BeatmapSetDiscordEmbed(embed, beatmaps.getOrNull(1)?.id, null, beatmap)
        }

        for ((index, beatmap) in beatmaps.withIndex()) {
            if (beatmap.id == beatmapId) {
                beatmap.beatmapSet = beatmapSet.cloneWithoutBeatmaps()
                val embed = self.getBeatmapEmbed(BeatmapRequest(beatmap.id, locale))
                return BeatmapSetDiscordEmbed(embed, beatmaps.getOrNull(index + 1)?.id, beatmaps.getOrNull(index - 1)?.id, beatmap)
            }
        }

        throw RuntimeException("Este beatmapId não faz parte deste BeatmapSet")
    }

    private fun Context.addVariable(key: String, value: Any): Context {
        this.setVariable(key, value)
        return this
    }
}