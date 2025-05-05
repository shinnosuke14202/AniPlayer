package com.example.aniplayer.exception

public class ParseException @JvmOverloads constructor(
    private val shortMessage: String?,
    private val url: String,
    cause: Throwable? = null,
) : RuntimeException("$shortMessage at $url", cause)
