package pt.mf.mybinder.data.repository

import pt.mf.mybinder.data.model.remote.CardResponse
import pt.mf.mybinder.data.model.remote.SearchResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.SearchRepository
import retrofit2.Response

/**
 * Created by Martim Ferreira on 08/02/2025
 */
class SearchRepositoryImpl : SearchRepository {
    override suspend fun fetchCards(
        query: String,
        pageSize: Int,
        pageNumber: Int,
        orderBy: String,
    ): Response<SearchResponse> {
        return HttpClient.api.fetchCards(query, pageSize, pageNumber, orderBy)
    }

    override suspend fun fetchCard(
        id: String
    ): Response<CardResponse> {
        return HttpClient.api.fetchCard(id)
    }
}