package pt.mf.mybinder.domain.usecase.remote

import pt.mf.mybinder.data.model.remote.SubTypesResponse
import pt.mf.mybinder.domain.repository.remote.SubTypesRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubTypesUseCase(private val repository: SubTypesRepository) {
    suspend fun fetchSubTypes(): Result<SubTypesResponse> {
        return repository.fetchSubTypes()
    }
}