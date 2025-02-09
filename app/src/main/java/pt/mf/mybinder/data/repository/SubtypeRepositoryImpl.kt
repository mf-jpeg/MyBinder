package pt.mf.mybinder.data.repository

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.local.MyBinderDatabase
import pt.mf.mybinder.data.local.dao.SubtypeDao
import pt.mf.mybinder.data.model.local.Subtype
import pt.mf.mybinder.data.model.remote.SubTypesResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.SubtypeRepository
import retrofit2.Response

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubtypeRepositoryImpl : SubtypeRepository {
    private val dao: SubtypeDao = MyBinderDatabase.instance.subTypeDao

    override suspend fun fetchRemoteSubtypes(): Response<SubTypesResponse> {
        return HttpClient.api.fetchSubTypes()
    }

    override suspend fun fetchLocalSubtypes(): Flow<List<Subtype>> {
        return dao.getAll()
    }

    override suspend fun insertSubType(subtype: Subtype) {
        dao.insertSubType(subtype)
    }

    override suspend fun insertSubTypes(subtypes: List<Subtype>) {
        dao.insertSubTypes(subtypes)
    }
}