package me.sknz.ousubot.infrastructure.tools

import me.sknz.ousubot.infrastructure.exceptions.osuNotFound
import net.dv8tion.jda.api.entities.Message
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * ## BeatmapDetector
 *
 * Um objeto que contém funções para detectar beatmaps e beatmapset
 * em uma mensagem ou texto.
 */
object BeatmapDetector {

    private const val BEATMAP_URL = "://osu.ppy.sh/beatmaps/"
    private const val BEATMAP_SHORT_URL = "://osu.ppy.sh/b/"
    private const val BEATMAPSET_URL = "://osu.ppy.sh/beatmapsets/"

    /**
     * Função para detectar um beatmap ou beatmapset
     * inserido em uma mensagem do Discord.
     *
     * A Ordem que ele irá procurar na mensagem é:
     * - Conteúdo da mensagem
     * - Campos de embeds
     *    - title.url
     *    - description
     *    - author.url
     *    - fields.value
     *
     * @param message Mensagem
     * @throws feign.FeignException.NotFound Caso não encontre nenhum Beatmap ou BeatmapSet
     */
    fun detect(message: Message): DetectorResult {
        val detected = detect(message.contentRaw)
        if (detected != null || message.embeds.isEmpty()) {
            return detected ?: osuNotFound("Não há beatmap ou beatmapset nesta mensagem!")
        }

        val embed = message.embeds[0]

        return detect(embed.url ?: "")
            ?: detect(embed.description ?: "")
            ?: detect(embed.author?.url ?: "")
            ?: embed.fields.stream().map { detect(it.value ?: "") }
                .findFirst()
                .orElse(null)
            ?: osuNotFound("Não há beatmap ou beatmapset nesta mensagem!")
    }

    /**
     * Função para detectar um beatmap ou beatmapset
     * inserido em um texto.

     * @param content texto que pode conter um link
     */
    fun detect(content: String): DetectorResult? {
        return detectBeatmapSet(content) ?: detectBeatmap(content)
    }

    private fun detectBeatmap(value: String): DetectorResult? {
        val indexes = let {
            val index = value.indexOf(BEATMAP_URL, 0 ,true)
            if (index == -1) {
                return@let arrayOf(value.indexOf(BEATMAP_SHORT_URL, 0, true), BEATMAP_SHORT_URL.length)
            }
            return@let arrayOf(index, BEATMAP_URL.length)
        }

        if (indexes[0] == -1) {
            return null
        }

        val content = value.substring(indexes[0] + indexes[1])
        val beatmap = arrayListOf<Int>()
        for (char in content.toCharArray()) {
            if (char.isDigit()) {
                beatmap.add(char.digitToInt())
                continue
            }
            break
        }
        if (beatmap.isEmpty()) {
            return null
        }

        return DetectorResult(beatmap.joinToString("").toInt())
    }

    private fun detectBeatmapSet(value: String): DetectorResult? {
        val index = value.indexOf(BEATMAPSET_URL, 0 ,true)
        if (index < 0) {
            return null
        }

        val content = value.substring(index + (BEATMAPSET_URL.length))
        val pattern: Pattern = Pattern.compile("([0-9]+)(#([a-zA-Z]+)/([0-9]+))?")
        val matcher: Matcher = pattern.matcher(content)

        if (!matcher.find()) {
            return null
        }

        return DetectorResult(matcher.group(4)?.toInt(), matcher.group(1).toInt())
    }

    data class DetectorResult(
        val beatmap: Int? = null,
        val beatmapSet: Int? = null
    )
}