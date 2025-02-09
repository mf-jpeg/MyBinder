package pt.mf.mybinder.data.repository

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.local.MyBinderDatabase
import pt.mf.mybinder.data.local.dao.SubTypeDao
import pt.mf.mybinder.data.model.local.SubType
import pt.mf.mybinder.data.model.remote.SubTypesResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.SubTypesRepository
import retrofit2.Response

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubTypesRepositoryImpl : SubTypesRepository {
    private val dao: SubTypeDao = MyBinderDatabase.instance.subTypeDao

    override suspend fun remoteFetchSubTypes(): Response<SubTypesResponse> {
        return HttpClient.api.fetchSubTypes()
    }

    override suspend fun localFetchSubTypes(): Flow<List<SubType>> {
        return dao.getAll()
    }

    override suspend fun insertSubType(subtype: SubType) {
        dao.insertSubType(subtype)
    }

    override suspend fun insertSubTypes(subtypes: List<SubType>) {
        dao.insertSubTypes(subtypes)
    }
}