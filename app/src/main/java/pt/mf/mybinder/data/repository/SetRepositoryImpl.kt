package pt.mf.mybinder.data.repository

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.local.MyBinderDatabase
import pt.mf.mybinder.data.local.dao.SetDao
import pt.mf.mybinder.data.model.local.Set
import pt.mf.mybinder.data.model.remote.SetResponse
import pt.mf.mybinder.data.remote.HttpClient
import pt.mf.mybinder.domain.repository.SetRepository
import retrofit2.Response

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SetRepositoryImpl() : SetRepository {
    private val dao: SetDao = MyBinderDatabase.instance.setDao

    override suspend fun fetchRemoteSets(): Response<SetResponse> {
        return HttpClient.api.fetchSets()
    }

    override suspend fun insertSet(set: Set) {
        dao.insertSet(set)
    }

    override suspend fun insertSets(sets: List<Set>) {
        dao.insertSets(sets)
    }

    override suspend fun fetchLocalSets(): Flow<List<Set>> {
        return dao.getAll()
    }

    override suspend fun fetchLocalSetById(id: String): Flow<Set?> {
        return dao.getById(id)
    }
}