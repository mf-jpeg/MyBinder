package pt.mf.mybinder.domain.repository

import pt.mf.mybinder.data.model.remote.CardDetailsResponse
import pt.mf.mybinder.data.model.remote.CardSearchResponse
import retrofit2.Response

/**
 * Created by Martim Ferreira on 08/02/2025
 */
interface CardRepository {
    suspend fun searchCard(
        name: String,
        pageSize: Int,
        pageNumber: Int,
    ): Response<CardSearchResponse>

    suspend fun fetchCardDetails(
        id: String
    ): Response<CardDetailsResponse>
}