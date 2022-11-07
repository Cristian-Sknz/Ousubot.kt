package me.sknz.ousubot.infrastructure.tools

import me.sknz.ousubot.app.api.models.enums.GameMode
import me.sknz.ousubot.app.api.params.UserScoreParameter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserScoreParameterTest {

    @Test
    fun parametros_para_requisicao_de_score() {
        val parameters = UserScoreParameter(UserScoreParameter.UserScoreType.BEST) {
            this.limit = 15
            this.mode = GameMode.Osu.name.lowercase()
        }

        Assertions.assertEquals("best?include_fails=0&limit=15&mode=osu",  parameters.toString())
    }
}