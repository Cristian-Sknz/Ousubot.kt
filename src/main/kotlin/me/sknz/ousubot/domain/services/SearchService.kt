package me.sknz.ousubot.domain.services

import me.sknz.ousubot.app.commands.BeatmapController
import me.sknz.ousubot.domain.dto.BeatmapSearchRequest
import me.sknz.ousubot.domain.dto.DiscordBeatmapEmbed
import me.sknz.ousubot.domain.services.type.CachedService
import me.sknz.ousubot.domain.services.type.OsuAPIService


/**
 * ## SearchService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de busca do Osu!
 *
 * @see BeatmapController
 */
interface SearchService<T: SearchService<T>> : CachedService<T>, OsuAPIService {

    /**
     * Função para pegar/gerar uma mensagem prontas.
     *
     * Por baixo dos panos essa função utiliza de cache em memória
     * para otimizar o processamento de uma requisição e evitar
     * requisições desnecessárias na API do Osu
     *
     * @throws feign.FeignException.NotFound Caso não encontre nenhum beatmap
     * @param request Uma requisição de pesquisa
     */
    fun getSearchEmbed(request: BeatmapSearchRequest): DiscordBeatmapEmbed
}