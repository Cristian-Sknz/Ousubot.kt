package me.sknz.ousubot.infrastructure.exceptions

import feign.FeignException.NotFound
import feign.FeignException.Unauthorized
import feign.Request
import feign.RequestTemplate

fun osuNotFound(message: String): Nothing = throw NotFound(message, getEmptyRequest(), Request.Body.empty().asBytes(), hashMapOf())
fun osuInvalidApiKey(message: String): Nothing = throw Unauthorized(message, getEmptyRequest(), Request.Body.empty().asBytes(), hashMapOf())
private fun getEmptyRequest(): Request {
    return Request.create(Request.HttpMethod.TRACE, "", hashMapOf(), null, RequestTemplate())
}