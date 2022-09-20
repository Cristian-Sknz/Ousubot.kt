package me.sknz.ousubot.interactions

import com.fasterxml.jackson.databind.ObjectMapper
import me.sknz.ousubot.api.adapter.OsuAccessToken
import me.sknz.ousubot.api.adapter.OsuOAuthCredentials
import me.sknz.ousubot.api.adapter.OsuTokenRepository
import me.sknz.ousubot.core.annotations.modal.ModalController
import me.sknz.ousubot.core.annotations.modal.ModalHandler
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.requests.RestAction
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.time.OffsetDateTime

@ModalController
class MiscModalController(
    private val auth: OsuOAuthCredentials,
    private val repository: OsuTokenRepository
) {

    @ModalHandler(id = "osu-token")
    fun token(event: ModalInteractionEvent): RestAction<*> {
        val token = event.getValue("token")!!.asString

        event.deferReply(true).complete().let {
            val client = OkHttpClient()
            val auth = auth.oauth
            val call = client.newCall(Request.Builder()
                .url("https://osu.ppy.sh/oauth/token")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .post(MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("client_id", auth["client-id"]!!)
                    .addFormDataPart("client_secret", auth["client-secret"]!!)
                    .addFormDataPart("redirect_uri", auth["redirect-uri"]!!)
                    .addFormDataPart("code", token)
                    .addFormDataPart("grant_type", "authorization_code")
                    .build())
                .build())

            val response = call.execute()
            if (response.isSuccessful) {
                val accessToken = createOsuAccessToken(response.body!!)
                repository.save(accessToken)
                return it.sendMessage("Concluído com sucesso!").setEphemeral(true)
            }

            return it.sendMessage("Ocorreu um erro ao tentar obter um token!\n ${response.message}")
        }
    }

    fun createOsuAccessToken(body: ResponseBody): OsuAccessToken {
        val json = ObjectMapper().readTree(body.byteStream())
        val entity = repository.findById(1).orElseGet { OsuAccessToken() }
        entity.id = 1
        entity.accessToken = json.get("access_token").asText()
        entity.createdDate = OffsetDateTime.now()
        entity.expireDate = OffsetDateTime.now().plusSeconds(json.get("expires_in").asLong())
        entity.refreshToken = json.get("refresh_token").asText()

        return entity
    }
}