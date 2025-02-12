package pt.mf.mybinder.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.mf.mybinder.utils.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Martim Ferreira on 08/02/2025
 */
object HttpClient {
    private const val BASE_URL = "https://api.pokemontcg.io/v2/"
    private const val BUILD_CONFIG_API_KEY_FIELD = "API_KEY"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // We use reflection in order to support building without specifying an api key.
    private val auth = Interceptor { chain ->
        if (!Utils.isBuildConfigFieldAvailable(BUILD_CONFIG_API_KEY_FIELD))
            chain.proceed(chain.request().newBuilder().build())

        val apiKey = Utils.getBuildConfigField(BUILD_CONFIG_API_KEY_FIELD)
        if (apiKey.isNullOrEmpty())
            chain.proceed(chain.request().newBuilder().build())

        val request = chain.request().newBuilder()
            .addHeader("X-Api-Key", apiKey!!)
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