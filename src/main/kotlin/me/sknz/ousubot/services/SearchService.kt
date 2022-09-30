package me.sknz.ousubot.services

import me.sknz.ousubot.dto.BeatmapSearchRequest
import me.sknz.ousubot.dto.DiscordBeatmapEmbed
import org.springframework.cache.annotation.Cacheable


/**
 * ## SearchService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de busca do Osu!
 *
 * @see me.sknz.ousubot.commands.BeatmapController
 */
interface SearchService<T> {


    /**
     * Pegar a instância desta classe como proxy.
     *
     * A anotação [Cacheable] funciona apenas na instância com proxy,
     * se for utilizado o `this` o caching não irã funcionar.
     */
    var self: T

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