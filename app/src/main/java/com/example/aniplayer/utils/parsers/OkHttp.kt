package com.example.aniplayer.utils.parsers

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Cookie
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Response
import okhttp3.ResponseBody
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    val callback = ContinuationCallCallback(this, continuation)
    enqueue(callback)
    continuation.invokeOnCancellation(callback)
}

val Response.mimeType: String?
    get() = header("content-type")?.substringBefore(';')?.trim()?.nullIfEmpty()?.lowercase()

val HttpUrl.isHttpOrHttps: Boolean
    get() = scheme.equals("https", ignoreCase = true) || scheme.equals("http", ignoreCase = true)

fun Headers.Builder.mergeWith(other: Headers, replaceExisting: Boolean): Headers.Builder {
    for ((name, value) in other) {
        if (replaceExisting || this[name] == null) {
            this[name] = value
        }
    }
    return this
}

fun Response.copy(): Response = newBuilder()
    .body(peekBody(Long.MAX_VALUE))
    .build()

fun Response.Builder.setHeader(name: String, value: String?): Response.Builder =
    if (value == null) {
        removeHeader(name)
    } else {
        header(name, value)
    }

@OptIn(ExperimentalContracts::class)
inline fun Response.map(mapper: (ResponseBody) -> ResponseBody): Response {
    contract {
        callsInPlace(mapper, InvocationKind.AT_MOST_ONCE)
    }
    return body?.use { responseBody ->
        newBuilder()
            .body(mapper(responseBody))
            .build()
    } ?: this
}

fun Cookie.newBuilder(): Cookie.Builder = Cookie.Builder().also { c ->
    c.name(name)
    c.value(value)
    if (persistent) {
        c.expiresAt(expiresAt)
    }
    if (hostOnly) {
        c.hostOnlyDomain(domain)
    } else {
        c.domain(domain)
    }
    c.path(path)
    if (secure) {
        c.secure()
    }
    if (httpOnly) {
        c.httpOnly()
    }
}
