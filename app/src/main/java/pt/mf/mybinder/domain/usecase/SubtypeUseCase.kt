package pt.mf.mybinder.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.Subtype
import pt.mf.mybinder.data.model.remote.SubTypesResponse
import pt.mf.mybinder.domain.repository.SubtypeRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubtypeUseCase(
    private val repository: SubtypeRepository
) : BaseUseCase() {

    suspend fun fetchRemoteSubtypes(): Result<SubTypesResponse> {
        return performHttpRequest {
            repository.fetchRemoteSubtypes()
        }
    }

    suspend fun insertSubType(subtype: Subtype) {
        repository.insertSubType(subtype)
    }

    suspend fun insertSubTypes(subtypes: List<Subtype>) {
        repository.insertSubTypes(subtypes)
    }

    suspend fun fetchLocalSubtypes(): Flow<List<Subtype>> {
        return repository.fetchLocalSubtypes()
    }

    fun convertRemoteToLocalSubType(subtype: String): Subtype {
        return Subtype(name = subtype)
    }
}