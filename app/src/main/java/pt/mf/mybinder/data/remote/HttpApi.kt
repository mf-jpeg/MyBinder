package pt.mf.mybinder.data.remote

import pt.mf.mybinder.data.model.remote.CardResponse
import pt.mf.mybinder.data.model.remote.SearchResponse
import pt.mf.mybinder.data.model.remote.SetResponse
import pt.mf.mybinder.data.model.remote.SubTypesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Martim Ferreira on 08/02/2025
 */
interface HttpApi {
    @GET("cards/")
    suspend fun fetchCards(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") pageNumber: Int,
        @Query("orderBy") orderBy: String
    ): Response<SearchResponse>

    @GET("cards/{id}")
    suspend fun fetchCard(
        @Path("id") id: String
    ): Response<CardResponse>

    @GET("sets")
    suspend fun fetchSets(): Response<SetResponse>

    @GET("subtypes")
    suspend fun fetchSubTypes(): Response<SubTypesResponse>
}