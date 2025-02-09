package pt.mf.mybinder.domain.usecase.remote

import pt.mf.mybinder.data.model.remote.SetResponse
import pt.mf.mybinder.domain.repository.remote.SetRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SetUseCase(private val repository: SetRepository) {
    suspend fun fetchSets(): Result<SetResponse> {
        return repository.fetchSets()
    }
}