package com.example.hokenbox.resource.injection

import android.content.Context
import com.example.hokenbox.BuildConfig
import com.example.hokenbox.data.local.Settings
import com.example.hokenbox.data.network.*
import com.example.hokenbox.resource.utils.isInternetAvailable
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthInterceptorClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @AuthInterceptorClient
    @Provides
    fun provideAuthClient(
        authInterceptor: AuthInterceptor,
        clientBuilder: OkHttpClient.Builder
    ): OkHttpClient = clientBuilder.apply {
        addInterceptor(authInterceptor)
    }.build()

    @NoAuthInterceptorClient
    @Provides
    fun provideNoAuthClient(
        clientBuilder: OkHttpClient.Builder
    ): OkHttpClient = clientBuilder.build()

    @Provides
    fun provideAuthClientBuilder(
        loggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
            addInterceptor(networkConnectionInterceptor)
        }
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}

class NetworkConnectionInterceptor @Inject constructor(@ApplicationContext private val context: Context) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.isInternetAvailable()) {
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No Internet Connection"
}

@Singleton
class AuthInterceptor @Inject constructor(
    private val apiService: RefreshTokenApiService,
    private val gson: Gson
) : Interceptor {
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val token = runBlocking { Settings.ACCESS_TOKEN.get("") }
        val res = chain.proceedWithToken(req, token)

//        if (res.code != HTTPError.UNAUTHORIZED.code) {
//            return res
//        }

        return res

//        val newToken: String? = runBlocking {
//            mutex.withLock {
//                when (SharedPref.getInstance().getRefreshToken()) {
//                    null, "" -> {
//                       // (MainApplication.visibleActivity as? MainActivity)?.logout()
//                        null
//                    } // already logged out!
//                    else -> {
//                        try {
//                            val router = ApiRouter(
//                                APIPath.refreshToken(), HTTPMethod.POST,
//                                parameters = toMap(
//                                    RefreshTokenRequest(
//                                        SharedPref.getInstance().getRefreshToken() ?: ""
//                                    )
//                                ).filterNotNullValues().toHashMap()
//                            )
//
//                            val refreshTokenRes = apiService
//                                .post(router.url(), router.headers, router.parameters)
//                            val result: Response<LoginResponse> =
//                                gson.fromJson(refreshTokenRes.string())
//                            result.data?.access_token?.let { SharedPref.getInstance().setToken(it) }
//                            result.data?.access_token
//                        } catch (e: Exception) {
//                            (MainApplication.visibleActivity as? MainActivity)?.logout()
//                            null
//                        }
//                    }
//                }
//            }
//        }
//        return if (newToken !== null) chain.proceedWithToken(req, newToken) else res
    }

    private fun Interceptor.Chain.proceedWithToken(req: Request, token: String?): Response =
        req.newBuilder()
            .apply {
                if (token !== null) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()
            .let(::proceed)
}