package me.sknz.ousubot.domain.services.impl

import me.sknz.ousubot.app.api.OsuClientAPI
import me.sknz.ousubot.app.api.models.beatmaps.BeatmapSearch
import me.sknz.ousubot.app.api.models.beatmaps.BeatmapSet
import me.sknz.ousubot.domain.dto.BeatmapSearchRequest
import me.sknz.ousubot.domain.dto.DiscordBeatmapEmbed
import me.sknz.ousubot.domain.services.SearchService
import me.sknz.ousubot.domain.utils.template
import me.sknz.ousubot.infrastructure.exceptions.osuNotFound
import me.sknz.ousubot.infrastructure.tools.CustomEmojis
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*
import java.util.stream.Collectors

@Service
class SearchServiceImpl(override val client: OsuClientAPI,
                        override val engine: SpringTemplateEngine,
                        private val emojis: CustomEmojis): SearchService<SearchServiceImpl> {

    @Autowired
    @Lazy
    override lateinit var self: SearchServiceImpl

    override fun getSearchEmbed(request: BeatmapSearchRequest): DiscordBeatmapEmbed {
        val search = self.search(request.decode())

        if (search.total == 0) {
            osuNotFound("Não foi encontrado nenhum beatmap na pesquisa ${request.decode()}")
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
                val embed = self.process(beatmapSet, query.locale)
                beatmapSet.beatmaps!![0].beatmapSet = beatmapSet.cloneWithoutBeatmaps()

                return DiscordBeatmapEmbed(embed, beatmapSets.getOrNull(index + 1)?.id, beatmapSets.getOrNull(index - 1)?.id, beatmap)
            }
        }

        throw RuntimeException("Este beatmapSetId não faz parte deste BeatmapSet")
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

    /**
     * Função para gerar uma mensagem pronta utilizando um engine template.
     *
     * @param beatmapSet [BeatmapSet] que será gerado em forma de [DiscordEmbed]
     * @see DiscordEmbed
     */
    fun process(beatmapSet: BeatmapSet, locale: Locale): DiscordEmbed {
        return template(engine) {
            template = "SearchEmbed"
            language = locale

            val beatmaps = beatmapSet.beatmaps!!
            val versions = beatmaps.stream()
                .map { "${emojis[it.mode.name]?.asMention} ${it.version} (${it.difficultyRating})" }
                .limit(5)
                .collect(Collectors.toList())

            if (beatmaps.size > 5) {
                versions.add("[......]")
            }

            variables["beatmapSet"] = beatmapSet
            variables["versions"] = versions
            variables["modes"] = beatmaps.map { it.mode.name }.distinct()
        }
    }

}