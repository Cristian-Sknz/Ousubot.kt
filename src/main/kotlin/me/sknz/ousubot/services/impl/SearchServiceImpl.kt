package me.sknz.ousubot.services.impl

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.api.models.beatmaps.BeatmapSearch
import me.sknz.ousubot.api.models.beatmaps.BeatmapSet
import me.sknz.ousubot.core.context.CustomEmojis
import me.sknz.ousubot.core.xml.DiscordEmbed
import me.sknz.ousubot.dto.BeatmapSearchRequest
import me.sknz.ousubot.dto.DiscordBeatmapEmbed
import me.sknz.ousubot.services.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.stream.Collectors

@Service
class SearchServiceImpl(
    private val client: OsuClientAPI,
    private val engine: SpringTemplateEngine,
    private val emojis: CustomEmojis
): SearchService<SearchServiceImpl> {

    @Autowired
    @Lazy
    override lateinit var self: SearchServiceImpl

    override fun getSearchEmbed(request: BeatmapSearchRequest): DiscordBeatmapEmbed {
        val search = self.search(request.decode())

        if (search.total == 0) {
            TODO("Não há beatmaps na pesquisa")
        }

        if (request.beatmapSet != null) {
            return self.getBeatmapSearchEmbed(request.withBeatmapSetId(request.beatmapSet!!), search)
        }

        return self.getBeatmapSearchEmbed(request.withBeatmapSetId(search.beatmapSets[0].id), search)
    }

    /**
     * Função para pegar/gerar uma mensagem prontas.
     *
     * NÃO UTILIZAR ESTÁ FUNÇÃO DIRETAMENTE,
     * utilize [SearchService.getSearchEmbed]
     *
     * @param query Uma requisição de pesquisa
     * @param search Uma pesquisa de beatmaps pronta
     */
    @Cacheable(cacheNames = ["embed:searchmaps"], key = "#query")
    fun getBeatmapSearchEmbed(query: BeatmapSearchRequest, search: BeatmapSearch): DiscordBeatmapEmbed {
        val beatmapSets = search.beatmapSets

        for ((index, beatmapSet) in beatmapSets.withIndex()) {
            if (beatmapSet.id == query.beatmapSet) {
                val beatmap = beatmapSet.beatmaps!![0]
                val embed = self.process(beatmapSet)
                beatmapSet.beatmaps!![0].beatmapSet = beatmapSet.cloneWithoutBeatmaps()

                return DiscordBeatmapEmbed(embed, beatmapSets.getOrNull(index + 1)?.id, beatmapSets.getOrNull(index - 1)?.id, beatmap)
            }
        }

        throw RuntimeException("Este beatmapSetId não faz parte deste BeatmapSet")
    }


    /**
     * Função para gerar uma mensagem pronta utilizando um engine template.
     *
     * @param beatmapSet [BeatmapSet] que será gerado em forma de [DiscordEmbed]
     * @see DiscordEmbed
     */
    fun process(beatmapSet: BeatmapSet): DiscordEmbed {
        val beatmaps = beatmapSet.beatmaps!!
        val versions = beatmaps.stream()
            .map { "${emojis.getEmoji(it.mode.name)?.asMention} ${it.version} (${it.difficultyRating})" }
            .limit(5).collect(Collectors.toList())

        if (beatmaps.size > 5) {
            versions.add("[......]")
        }

        val ctx = Context()
            .addVariable("beatmapSet", beatmapSet)
            .addVariable("versions", versions)
            .addVariable("modes", beatmaps.map { it.mode.name }.distinct())

        val xml = engine.process("SearchEmbed", ctx)

        val mapper = XmlMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return mapper.readValue(xml, DiscordEmbed::class.java)
    }

    /**
     * Função para fazer uma requisição utilizando [OsuClientAPI],
     * e guardar o resultado em cache.
     *
     * @param query input de pesquisa
     * @see BeatmapSearch
     */
    @Cacheable(cacheNames=["api:searchmaps"], key = "#query")
    fun search(query: String): BeatmapSearch {
        return client.searchBeatmap(query)
    }

    private fun Context.addVariable(key: String, value: Any): Context {
        this.setVariable(key, value)
        return this
    }
}