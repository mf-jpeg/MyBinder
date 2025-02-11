package pt.mf.mybinder.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.Set
import pt.mf.mybinder.data.model.remote.SetResponse
import retrofit2.Response

/**
 * Created by Martim Ferreira on 09/02/2025
 */
interface SetRepository {
    suspend fun fetchRemoteSets(): Response<SetResponse>
    suspend fun fetchLocalSets(): Flow<List<Set>>
    suspend fun fetchLocalSetById(id: String): Flow<Set?>
    suspend fun fetchLocalSetIdByName(name: String): Flow<String?>
    suspend fun insertSet(set: Set)
    suspend fun insertSets(sets: List<Set>)
}