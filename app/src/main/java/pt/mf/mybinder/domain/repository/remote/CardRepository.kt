package pt.mf.mybinder.domain.repository.remote

import pt.mf.mybinder.data.model.remote.CardDetailsResponse
import pt.mf.mybinder.data.model.remote.CardSearchResponse
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 08/02/2025
 */
abstract class CardRepository : BaseRepository() {
    abstract suspend fun searchCard(
        name: String,
        pageSize: Int,
        pageNumber: Int,
    ): Result<CardSearchResponse>

    abstract suspend fun fetchCardDetails(
        id: String
    ): Result<CardDetailsResponse>
}