package pt.mf.mybinder.data.remote

import pt.mf.mybinder.data.model.CardDetailsResponse
import pt.mf.mybinder.data.model.CardSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Martim Ferreira on 08/02/2025
 */
interface HttpApi {
    @GET("cards/")
    suspend fun searchCard(
        @Query("q") name: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") pageNumber: Int
    ): Response<CardSearchResponse>

    @GET("cards/{id}")
    suspend fun fetchCardDetails(
        @Path("id") id: String
    ): Response<CardDetailsResponse>
}