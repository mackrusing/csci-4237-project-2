package net.mackk.metroview

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object API {

    val okHttpClient: OkHttpClient

    init {
        // create builder
        val builder = OkHttpClient.Builder()

        // create logger
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        // set client
        okHttpClient = builder.build()
    }

}
