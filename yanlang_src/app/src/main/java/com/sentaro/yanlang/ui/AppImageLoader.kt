package com.sentaro.yanlang.ui

import android.content.Context
import coil.disk.DiskCache
import coil.memory.MemoryCache
import okio.Path.Companion.toOkioPath
import coil.ImageLoader

object AppImageLoader {
    private var instance: ImageLoader? = null

    fun get(context: Context): ImageLoader {
        return instance ?: ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache").toOkioPath())
                    .maxSizeBytes(50L * 1024 * 1024)
                    .build()
            }
            .build()
            .also { instance = it }
    }
}
