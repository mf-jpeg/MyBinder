package pt.mf.mybinder.data.repository.remote

import pt.mf.mybinder.data.model.remote.SubTypesResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.remote.SubTypesRepository
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubTypesRepositoryImpl : SubTypesRepository() {
    override suspend fun fetchSubTypes(): Result<SubTypesResponse> {
        return performHttpRequest { HttpClient.api.fetchSubTypes() }
    }
}