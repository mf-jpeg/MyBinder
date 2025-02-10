package pt.mf.mybinder.domain.repository

import pt.mf.mybinder.data.model.remote.CardResponse
import pt.mf.mybinder.data.model.remote.SearchResponse
import retrofit2.Response

/**
 * Created by Martim Ferreira on 08/02/2025
 */
interface SearchRepository {
    suspend fun fetchCards(
        query: String,
        pageSize: Int,
        pageNumber: Int,
    ): Response<SearchResponse>

    suspend fun fetchCard(
        id: String
    ): Response<CardResponse>
}