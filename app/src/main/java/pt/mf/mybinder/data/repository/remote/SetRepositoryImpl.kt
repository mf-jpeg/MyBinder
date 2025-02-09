package pt.mf.mybinder.data.repository.remote

import pt.mf.mybinder.data.model.remote.SetResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.remote.SetRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SetRepositoryImpl : SetRepository() {
    override suspend fun fetchSets(): Result<SetResponse> {
        return performHttpRequest { HttpClient.api.fetchSets() }
    }
}