package pt.mf.mybinder.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.SubType
import pt.mf.mybinder.data.model.remote.SubTypesResponse
import pt.mf.mybinder.domain.repository.SubTypesRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubTypesUseCase(
    private val repository: SubTypesRepository
) : BaseUseCase() {

    suspend fun remoteFetchSubTypes(): Result<SubTypesResponse> {
        return performHttpRequest {
            repository.remoteFetchSubTypes()
        }
    }

    suspend fun insertSubType(subtype: SubType) {
        repository.insertSubType(subtype)
    }

    suspend fun insertSubTypes(subtypes: List<SubType>) {
        repository.insertSubTypes(subtypes)
    }

    suspend fun localFetchSubTypes(): Flow<List<SubType>> {
        return repository.localFetchSubTypes()
    }

    fun convertRemoteToLocalSubType(subtype: String): SubType {
        return SubType(name = subtype)
    }
}