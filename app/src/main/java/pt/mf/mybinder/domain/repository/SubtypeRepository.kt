package pt.mf.mybinder.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.Subtype
import pt.mf.mybinder.data.model.remote.SubTypesResponse
import retrofit2.Response

/**
 * Created by Martim Ferreira on 09/02/2025
 */
interface SubtypeRepository {
    suspend fun fetchRemoteSubtypes(): Response<SubTypesResponse>
    suspend fun fetchLocalSubtypes(): Flow<List<Subtype>>
    suspend fun insertSubType(subtype: Subtype)
    suspend fun insertSubTypes(subtypes: List<Subtype>)
}