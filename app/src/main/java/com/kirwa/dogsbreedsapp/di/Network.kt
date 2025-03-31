package com.kirwa.dogsbreedsapp.di

//import com.tech.dvtweatherapp.BuildConfig
import com.kirwa.dogsbreedsapp.BuildConfig
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * createNetworkClient
 */
fun createNetworkClient(
    baseUrl: String,
    apiKey: String
) =
    retrofitClient(
        httpClient(apiKey),
        baseUrl
    )

private class BasicAuthInterceptor(val apiKey: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl: HttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("x-api-key", apiKey)
            .build()
        val requestBuilder: Request.Builder = original.newBuilder().url(url)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}

/**
 * httpClient
 */
fun httpClient(apiKey: String): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    val clientBuilder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(httpLoggingInterceptor)
    }
    clientBuilder.addInterceptor(BasicAuthInterceptor(apiKey))
    clientBuilder.readTimeout(120, TimeUnit.SECONDS)
    clientBuilder.writeTimeout(120, TimeUnit.SECONDS)

    return clientBuilder.build()
}

private fun retrofitClient(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
