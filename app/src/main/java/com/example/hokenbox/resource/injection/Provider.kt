package com.example.hokenbox.resource.injection

import com.example.hokenbox.BuildConfig
import com.example.hokenbox.data.network.ApiService
import com.example.hokenbox.data.network.ApiServiceNoLogin
import com.example.hokenbox.data.network.RefreshTokenApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object Provider {

    @Provides
    fun provideRetrofit(
        @AuthInterceptorClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideRetrofitNoAuth(
        @NoAuthInterceptorClient okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): ApiServiceNoLogin = retrofitBuilder
        .client(okHttpClient)
        .build()
        .create(ApiServiceNoLogin::class.java)

    @Provides
    fun provideRetrofitRefreshToken(
        @NoAuthInterceptorClient okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): RefreshTokenApiService = retrofitBuilder
        .client(okHttpClient)
        .build()
        .create(RefreshTokenApiService::class.java)

    @Provides
    fun provideRetrofitBuilder(gsonConverterFactory: GsonConverterFactory): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(gsonConverterFactory)


//    @Provides
//    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java, "gurume-go"
//        ).build()
//    }

//    @Provides
//    fun provideHistoryDao(@ApplicationContext context: Context): SearchHistoryDao {
//        return provideAppDataBase(context).userDao()
//    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .generateNonExecutableJson()
            .create()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}