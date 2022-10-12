package me.sknz.ousubot.app.api.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Component
class OsuTokenClient(
    private val auth: OsuOAuthCredentials,
    private val repository: OsuTokenRepository
) {

    private val client: OkHttpClient = OkHttpClient()

    fun getToken(authorizationCode: String): Call {
        val auth = auth.oauth
        return client.newCall(
            Request.Builder()
                .url("https://osu.ppy.sh/oauth/token")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .post(
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("client_id", auth["client-id"]!!)
                        .addFormDataPart("client_secret", auth["client-secret"]!!)
                        .addFormDataPart("redirect_uri", auth["redirect-uri"]!!)
                        .addFormDataPart("code", authorizationCode)
                        .addFormDataPart("grant_type", "authorization_code")
                        .build()
                )
                .build()
        )
    }

    fun getRenewedToken(refreshToken: String): Call {
        val auth = auth.oauth
        return client.newCall(
            Request.Builder()
                .url("https://osu.ppy.sh/oauth/token")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .post(
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("client_id", auth["client-id"]!!)
                        .addFormDataPart("client_secret", auth["client-secret"]!!)
                        .addFormDataPart("redirect_uri", auth["redirect-uri"]!!)
                        .addFormDataPart("refresh_token", refreshToken)
                        .addFormDataPart("grant_type", "refresh_token")
                        .build()
                )
                .build()
        )
    }

    fun getAccessToken(response: Response): OsuAccessToken {
        val json = ObjectMapper().readTree(response.body!!.byteStream())
        val entity = repository.findById(1).orElseGet { OsuAccessToken() }
        entity.id = 1
        entity.accessToken = json.get("access_token").asText()
        entity.createdDate = OffsetDateTime.now()
        entity.expireDate = OffsetDateTime.now()
            .plus(json.get("expires_in").asLong(), ChronoUnit.SECONDS)
        entity.refreshToken = json.get("refresh_token").asText()

        return entity
    }
}