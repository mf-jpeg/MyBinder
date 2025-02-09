package pt.mf.mybinder.data.repository

import pt.mf.mybinder.data.model.remote.CardDetailsResponse
import pt.mf.mybinder.data.model.remote.CardSearchResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.CardRepository
import retrofit2.Response

/**
 * Created by Martim Ferreira on 08/02/2025
 */
class CardRepositoryImpl : CardRepository {
    override suspend fun searchCard(
        name: String,
        pageSize: Int,
        pageNumber: Int
    ): Response<CardSearchResponse> {
        return HttpClient.api.searchCard("name:$name", pageSize, pageNumber)
    }

    override suspend fun fetchCardDetails(
        id: String
    ): Response<CardDetailsResponse> {
        return HttpClient.api.fetchCardDetails(id)
    }
}