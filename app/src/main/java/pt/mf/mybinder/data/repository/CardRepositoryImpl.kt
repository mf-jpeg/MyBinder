package pt.mf.mybinder.data.repository

import pt.mf.mybinder.data.model.CardDetailsResponse
import pt.mf.mybinder.data.model.CardSearchResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.CardRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 08/02/2025
 */
class CardRepositoryImpl : CardRepository() {
    override suspend fun searchCard(
        name: String,
        pageSize: Int,
        pageNumber: Int
    ): Result<CardSearchResponse> {
        return performHttpRequest {
            HttpClient.api.searchCard("name:$name", pageSize, pageNumber)
        }
    }

    override suspend fun fetchCardDetails(id: String): Result<CardDetailsResponse> {
        return performHttpRequest {
            HttpClient.api.fetchCardDetails(id)
        }
    }
}