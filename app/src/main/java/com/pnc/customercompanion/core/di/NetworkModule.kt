package com.pnc.customercompanion.core.di

import com.pnc.customercompanion.BuildConfig
import com.pnc.customercompanion.features.orders.data.OrderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

// how long a request takes before it gives up
private const val TIMEOUT_SECONDS = 15L

//builds the shared network pieces once for  the whole app

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // the json reader which skips fields the app doesn't use
    @Provides
    @Singleton
    fun providesJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    // the HTTP client, gives up after the timeout, logs only in debug builds
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        if(BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    //Retrofit, pointed at the base URL, reading JSON with teh shared reader
    @Provides
    @Singleton
    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        val jsonConverter = json.asConverterFactory("application/json".toMediaType())
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }

}
