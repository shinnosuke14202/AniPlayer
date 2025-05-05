package com.example.aniplayer.network

import com.example.aniplayer.exception.AuthRequiredException
import com.example.aniplayer.exception.NotFoundException
import com.example.aniplayer.model.site.Source
import com.example.aniplayer.utils.parsers.await
import com.example.aniplayer.utils.parsers.parseJson
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import org.jsoup.HttpStatusException
import java.net.HttpURLConnection

class OkHttpWebClient(
    private val httpClient: OkHttpClient,
    private val source: Source,
) : WebClient {

    override suspend fun httpGet(url: HttpUrl, extraHeaders: Headers?): Response {
        val request = Request.Builder().url(url).addTags().addExtraHeaders(extraHeaders).get()
        return httpClient.newCall(request.build()).await().ensureSuccess()
    }

    override suspend fun httpHead(url: HttpUrl): Response {
        val request = Request.Builder().head().url(url).addTags()
        return httpClient.newCall(request.build()).await().ensureSuccess()
    }

    override suspend fun httpPost(
        url: HttpUrl, form: Map<String, String>, extraHeaders: Headers?
    ): Response {
        val body = FormBody.Builder()
        form.forEach { (k, v) ->
            body.addEncoded(k, v)
        }
        val request =
            Request.Builder().post(body.build()).url(url).addTags().addExtraHeaders(extraHeaders)
        return httpClient.newCall(request.build()).await().ensureSuccess()
    }

    override suspend fun httpPost(url: HttpUrl, payload: String, extraHeaders: Headers?): Response {
        val body = FormBody.Builder()
        payload.split('&').forEach {
            val pos = it.indexOf('=')
            if (pos != -1) {
                val k = it.substring(0, pos)
                val v = it.substring(pos + 1)
                body.addEncoded(k, v)
            }
        }
        val request =
            Request.Builder().post(body.build()).url(url).addTags().addExtraHeaders(extraHeaders)
        return httpClient.newCall(request.build()).await().ensureSuccess()
    }

    override suspend fun httpPost(
        url: HttpUrl, body: JSONObject, extraHeaders: Headers?
    ): Response {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toString().toRequestBody(mediaType)
        val request =
            Request.Builder().post(requestBody).url(url).addTags().addExtraHeaders(extraHeaders)
        return httpClient.newCall(request.build()).await().ensureSuccess()
    }

    override suspend fun graphQLQuery(endpoint: String, query: String): JSONObject {
        val body = JSONObject()
        body.put("operationName", null as Any?)
        body.put("variables", JSONObject())
        body.put("query", "{$query}")

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toString().toRequestBody(mediaType)
        val request = Request.Builder().post(requestBody).url(endpoint).addTags()
        val json = httpClient.newCall(request.build()).await().parseJson()
        json.optJSONArray("errors")?.let {
            // TODO
        }
        return json
    }

    private fun Request.Builder.addTags(): Request.Builder {
        tag(Source::class.java, source)
        return this
    }

    private fun Request.Builder.addExtraHeaders(headers: Headers?): Request.Builder {
        if (headers != null) {
            headers(headers)
        }
        return this
    }

    private fun Response.ensureSuccess(): Response {
        val exception: Exception? = when (code) { // Catch some error codes, not all
            HttpURLConnection.HTTP_NOT_FOUND -> NotFoundException(message, request.url.toString())
            HttpURLConnection.HTTP_UNAUTHORIZED -> request.tag(Source::class.java)?.let {
                AuthRequiredException(it)
            } ?: HttpStatusException(message, code, request.url.toString())
            in 400..599 -> HttpStatusException(message, code, request.url.toString())
            else -> null
        }
        if (exception != null) {
            runCatching {
                close()
            }.onFailure {
                exception.addSuppressed(it)
            }
            throw exception
        }
        return this
    }
}
