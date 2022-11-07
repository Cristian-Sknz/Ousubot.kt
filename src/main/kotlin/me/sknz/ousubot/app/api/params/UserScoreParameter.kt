package me.sknz.ousubot.app.api.params

import me.sknz.ousubot.app.api.models.enums.GameMode
import me.sknz.ousubot.infrastructure.tools.toCamelCase
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import java.io.Serializable
import java.util.stream.Collectors
import kotlin.reflect.full.declaredMembers

class UserScoreParameter(
    var type: UserScoreType = UserScoreType.BEST,
    val query: Query = Query()
): Serializable {
    constructor(type: UserScoreType, query: Query.() -> Unit) : this(type, Query().apply(query))

    data class Query(
        var includeFails: Int = 0,
        var mode: String? = null,
        var limit: Int? = null,
        var offset: Int? = null
    ) : Serializable

    enum class UserScoreType : Serializable {
        BEST, FIRSTS, RECENT;

        val value = name.lowercase()

        companion object {
            fun of(name: String?) = UserScoreType.values().find { it.value.equals(name, true) }
        }
    }

    override fun toString(): String {
        val query = this.query::class.declaredMembers
            .stream()
            .limit(4).map {
                it.call(this.query)?.let { value ->
                    return@let "${it.name.toCamelCase()}=${value}"
                }
            }.collect(Collectors.toList()).filterNotNull()

        if (query.isEmpty()) {
            return type.value
        }

        return "${type.value}?${query.joinToString("&")}"
    }

    companion object {
        fun from(options: Collection<OptionMapping>): UserScoreParameter {
            val type = UserScoreType.of(options["type"]) ?: UserScoreType.BEST
            return UserScoreParameter(type) {
                mode = GameMode.of(options["mode"])?.name?.lowercase()
            }
        }

        private operator fun Collection<OptionMapping>.get(value: String): String? {
            return this.find { it.name.equals(value, true) }?.asString
        }
    }
}