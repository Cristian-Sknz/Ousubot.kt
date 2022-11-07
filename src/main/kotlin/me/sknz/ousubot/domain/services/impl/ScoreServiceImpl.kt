package me.sknz.ousubot.domain.services.impl

import me.sknz.ousubot.app.api.OsuClientAPI
import me.sknz.ousubot.app.api.models.scores.BeatmapScore
import me.sknz.ousubot.app.api.models.scores.BeatmapScores
import me.sknz.ousubot.domain.dto.DiscordScoreEmbed
import me.sknz.ousubot.domain.dto.DiscordUserScoreEmbed
import me.sknz.ousubot.domain.dto.ScoreRequest
import me.sknz.ousubot.domain.dto.UserScoreRequest
import me.sknz.ousubot.domain.services.ScoreService
import me.sknz.ousubot.domain.utils.template
import me.sknz.ousubot.infrastructure.annotations.WorkInProgress
import me.sknz.ousubot.infrastructure.exceptions.osuNotFound
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*

@WorkInProgress
@Service
class ScoreServiceImpl(
    override val client: OsuClientAPI,
    override val engine: SpringTemplateEngine,
    val beatmapService: BeatmapServiceImpl
) : ScoreService<ScoreServiceImpl> {

    @Autowired
    @Lazy
    override lateinit var self: ScoreServiceImpl

    override fun getBeatmapScoreEmbed(request: ScoreRequest): DiscordScoreEmbed {
        val scores = self.getBeatmapScore(request.beatmap)
        if (request.score == null) {
            request.score = scores[0].id
            return self.getBeatmapScoreEmbed(request, scores)
        }

        return self.getBeatmapScoreEmbed(request, scores)
    }

    override fun getUserScoreEmbed(request: UserScoreRequest): DiscordUserScoreEmbed {
        val scores = self.getUserScore(request)
        if (scores.isEmpty()) {
            osuNotFound("Não foi encontrado resultados de scores.")
        }

        if (request.score == null) {
            request.score = scores[0].id
            return self.getUserScoreEmbed(request, scores)
        }

        return self.getUserScoreEmbed(request, scores)
    }

    @Cacheable(cacheNames = ["embed:beatmapscore"], key = "#request")
    fun getBeatmapScoreEmbed(request: ScoreRequest, beatmapScores: BeatmapScores): DiscordScoreEmbed {
        val scores = beatmapScores.scores
        for ((index, score) in scores.withIndex()) {
            if (score.id != request.score) {
                continue
            }
            val embed = process(score, request.locale)
            return DiscordScoreEmbed(embed,
                scores.getOrNull(index + 1)?.id,
                scores.getOrNull(index - 1)?.id,
                score.beatmap!!.id)
        }

        throw RuntimeException("Este scoreId não faz parte deste BeatmapScore")
    }

    @Cacheable(cacheNames = ["embed:userscore"], key = "#request")
    fun getUserScoreEmbed(request: UserScoreRequest, beatmapScores: BeatmapScores): DiscordUserScoreEmbed {
        val scores = beatmapScores.scores
        for ((index, score) in beatmapScores.withIndex()) {
            if (score.id != request.score) {
                continue
            }
            val embed = process(score, request.locale)
            return DiscordUserScoreEmbed(embed,
                scores.getOrNull(index + 1)?.id,
                scores.getOrNull(index - 1)?.id,
                Pair(request.userId, request.parameters))
        }

        throw RuntimeException("Este scoreId não faz parte deste BeatmapScore")
    }

    @Cacheable(cacheNames = ["api:userscore"], key = "#request")
    fun getUserScore(request: UserScoreRequest): BeatmapScores {
        val score = BeatmapScores(client.getUserScore(request.userId, request.parameters).toTypedArray())
        score.scores.forEach {
            it.beatmap!!.beatmapSet = it.beatmapSet
        }
        return score
    }

    @Cacheable(cacheNames = ["api:beatmapscore"], key = "#beatmapId")
    fun getBeatmapScore(beatmapId: Int): BeatmapScores {
        val beatmapSet = beatmapService.getBeatmapSetByBeatmapId(beatmapId)

        val score = client.getBeatmapScore(beatmapId)
        score.scores.forEach {
            it.beatmap = beatmapSet[beatmapId]
            it.beatmap!!.beatmapSet = beatmapSet
        }

        return score
    }

    fun process(score: BeatmapScore, locale: Locale): DiscordEmbed {
        return template(engine) {
            this.template = "BeatmapScoreEmbed"
            this.language = locale

            this.variables["score"] = score
            this.variables["beatmap"] = score.beatmap!!
        }
    }

}