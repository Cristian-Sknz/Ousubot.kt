package me.sknz.ousubot.domain.services

import me.sknz.ousubot.app.commands.ScoreController
import me.sknz.ousubot.domain.dto.DiscordScoreEmbed
import me.sknz.ousubot.domain.dto.DiscordUserScoreEmbed
import me.sknz.ousubot.domain.dto.ScoreRequest
import me.sknz.ousubot.domain.dto.UserScoreRequest
import me.sknz.ousubot.domain.services.type.CachedService
import me.sknz.ousubot.domain.services.type.OsuAPIService

/**
 * ## ScoreService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de pontuações do Osu!
 *
 * @see ScoreController
 */
interface ScoreService<T: ScoreService<T>>: CachedService<T>, OsuAPIService {

    /**
     * Função para pegar/gerar uma mensagem prontas.
     *
     * Por baixo dos panos essa função utiliza de cache em memória
     * para otimizar o processamento de uma requisição e evitar
     * requisições desnecessárias na API do Osu
     *
     * @param request Uma requisição de score
     */
    fun getBeatmapScoreEmbed(request: ScoreRequest): DiscordScoreEmbed

    fun getUserScoreEmbed(request: UserScoreRequest): DiscordUserScoreEmbed
}