package me.sknz.ousubot.services.impl

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.api.models.beatmaps.Beatmap
import me.sknz.ousubot.api.models.beatmaps.BeatmapSet
import me.sknz.ousubot.core.xml.DiscordEmbed
import me.sknz.ousubot.dto.BeatmapSetRequest
import me.sknz.ousubot.dto.DiscordBeatmapEmbed
import me.sknz.ousubot.services.BeatmapService
import me.sknz.ousubot.utils.ColorThief
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*

@Service
class BeatmapServiceImpl(
    private val client: OsuClientAPI,
    private val engine: SpringTemplateEngine,
): BeatmapService<BeatmapServiceImpl> {

    @Autowired
    @Lazy
    override lateinit var self: BeatmapServiceImpl

    override fun getBeatmapEmbed(request: BeatmapSetRequest): DiscordBeatmapEmbed {
        if (request.beatmap != null && request.id == null) {
            val beatmapSet = self.getBeatmapSetByBeatmapId(request.beatmap!!)
            request.id = beatmapSet.id
            return self.getBeatmapSetEmbed(request, beatmapSet)
        }

        val beatmapSet = self.getBeatmapSet(request.id!!)
        if (request.beatmap == null) {
            request.beatmap = beatmapSet.beatmaps!!.sortedBy { it.difficultyRating }.first().id
        }
        return self.getBeatmapSetEmbed(request, beatmapSet)
    }

    /**
     * Função para pegar/gerar uma mensagem prontas.
     *
     * NÃO UTILIZAR ESTÁ FUNÇÃO DIRETAMENTE,
     * utilize [BeatmapService.getBeatmapEmbed]
     *
     * @param request Uma requisição de pesquisa
     * @param beatmapSet BeatmapSet que irá gerar o [DiscordEmbed]
     */
    @Cacheable(cacheNames = ["embed:beatmapsets"], key = "#request")
    fun getBeatmapSetEmbed(request: BeatmapSetRequest, beatmapSet: BeatmapSet): DiscordBeatmapEmbed {
        val beatmaps = beatmapSet.beatmaps!!
        for ((index, beatmap) in beatmaps.withIndex()) {
            if (beatmap.id == request.beatmap) {
                beatmap.beatmapSet = beatmapSet.cloneWithoutBeatmaps()
                val embed = process(beatmap, request.locale)
                return DiscordBeatmapEmbed(embed, beatmaps.getOrNull(index + 1)?.id, beatmaps.getOrNull(index - 1)?.id, beatmap)
            }
        }

        throw RuntimeException("Este beatmapId não faz parte deste BeatmapSet")
    }

    /**
     * Função para fazer uma requisição utilizando [OsuClientAPI],
     * e guardar o resultado em cache.
     *
     * @param id ID do beatmapset
     * @see BeatmapSet
     */
    @Cacheable(cacheNames=["api:beatmapsets"], key = "#id")
    fun getBeatmapSet(id: Int): BeatmapSet {
        return client.getBeatmapSet(id)
    }

    /**
     * Função para fazer uma requisição utilizando [OsuClientAPI],
     * e guardar o resultado em cache.
     *
     * @param id ID do beatmap
     * @see BeatmapSet
     */
    @Cacheable(cacheNames = ["api:lockup"], key = "#id")
    fun getBeatmapSetByBeatmapId(id: Int): BeatmapSet {
        return client.lockupBeatmapSet(id)
    }

    /**
     * Função para gerar uma mensagem pronta utilizando um engine template.
     *
     * @param beatmap [Beatmap] que será gerado em forma de [DiscordEmbed]
     * @see DiscordEmbed
     */
    fun process(beatmap: Beatmap, locale: Locale): DiscordEmbed {
        val ctx = Context()
            .addVariable("beatmap", beatmap)
            .addVariable("color", ColorThief.getPredominatColor(beatmap.beatmapSet!!.covers.card, true).rgb)

        val xml = engine.process("BeatmapInfo", ctx)

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