package com.example.aniplayer.site

import com.example.aniplayer.model.site.Source
import com.example.aniplayer.network.OkHttpWebClient
import com.example.aniplayer.network.WebClient
import com.example.aniplayer.utils.DEFAULT_OFFSET
import com.example.aniplayer.utils.parsers.LONG_HASH_SEED
import okhttp3.CookieJar
import okhttp3.OkHttpClient

abstract class Site {
    open lateinit var domain: String
    open lateinit var source: Source
    open var offset: Int = DEFAULT_OFFSET

    private val cookieJar: CookieJar = CookieJar.NO_COOKIES

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .build()

    val webClient: WebClient by lazy {
        OkHttpWebClient(httpClient, source)
    }

    open fun generateUid(url: String): Long {
        var h: Long = LONG_HASH_SEED
        source.name.forEach { c ->
            h = 31 * h + c.code.toLong()
        }
        url.forEach { c ->
            h = 31 * h + c.code.toLong()
        }
        return h
    }
}
