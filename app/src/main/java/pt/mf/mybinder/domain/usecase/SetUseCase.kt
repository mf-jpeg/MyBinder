package pt.mf.mybinder.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.remote.SetResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.SetRepository
import pt.mf.mybinder.utils.Result
import pt.mf.mybinder.data.model.local.Set as LocalSet
import pt.mf.mybinder.domain.model.remote.Set as RemoteSet

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SetUseCase(private val repository: SetRepository) : BaseUseCase() {
    suspend fun fetchRemoteSets(): Result<SetResponse> {
        return performHttpRequest { HttpClient.api.fetchSets() }
    }

    suspend fun insertSet(set: LocalSet) {
        repository.insertSet(set)
    }

    suspend fun insertSets(sets: List<LocalSet>) {
        repository.insertSets(sets)
    }

    suspend fun fetchLocalSets(): Flow<List<LocalSet>> {
        return repository.fetchLocalSets()
    }

    suspend fun fetchLocalSetById(id: String): Flow<LocalSet?> {
        return repository.fetchLocalSetById(id)
    }

    suspend fun fetchLocalSetIdByName(name: String): Flow<String?> {
        return repository.fetchLocalSetIdByName(name)
    }

    fun convertRemoteToLocalSet(set: RemoteSet): LocalSet {
        return LocalSet(
            id = set.id,
            name = set.name,
            total = set.total,
            printedTotal = set.printedTotal,
            releaseDate = set.releaseDate,
            iconUrl = set.images.icon
        )
    }
}