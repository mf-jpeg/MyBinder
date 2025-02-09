package pt.mf.mybinder.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.mf.mybinder.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Martim Ferreira on 08/02/2025
 */
object HttpClient {
    private const val BASE_URL = "https://api.pokemontcg.io/v2/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val auth = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("X-Api-Key", BuildConfig.API_KEY)
            .build()

        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(auth)
        .addInterceptor(logging)
        .build()

    val api: HttpApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HttpApi::class.java)
    }
}