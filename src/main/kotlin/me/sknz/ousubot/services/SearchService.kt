package me.sknz.ousubot.services

import me.sknz.ousubot.dto.BeatmapSearchRequest
import me.sknz.ousubot.dto.DiscordBeatmapEmbed


/**
 * ## SearchService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de busca do Osu!
 *
 * @see me.sknz.ousubot.commands.BeatmapController
 */
interface SearchService<T> : CachedService<T> {

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