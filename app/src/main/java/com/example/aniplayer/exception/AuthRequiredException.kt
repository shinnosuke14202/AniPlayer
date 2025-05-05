package com.example.aniplayer.exception

import com.example.aniplayer.model.site.Source
import okio.IOException

/**
 * Authorization is required for access to the requested content
 */
class AuthRequiredException @JvmOverloads constructor(
    val source: Source,
    cause: Throwable? = null,
) : IOException("Authorization required", cause)
