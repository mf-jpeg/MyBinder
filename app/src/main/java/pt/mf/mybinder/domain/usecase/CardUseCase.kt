package pt.mf.mybinder.domain.usecase

import pt.mf.mybinder.data.model.remote.CardDetailsResponse
import pt.mf.mybinder.data.model.remote.CardSearchResponse
import pt.mf.mybinder.domain.repository.CardRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class CardUseCase(private val repository: CardRepository) : BaseUseCase() {
    private companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_PAGE_NUMBER = 1
    }

    suspend fun searchCard(
        name: String,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
    ): Result<CardSearchResponse> {
        return performHttpRequest {
            repository.searchCard(name, pageSize, pageNumber)
        }
    }

    suspend fun fetchCardDetails(
        id: String
    ): Result<CardDetailsResponse> {
        return performHttpRequest {
            repository.fetchCardDetails(id)
        }
    }
}