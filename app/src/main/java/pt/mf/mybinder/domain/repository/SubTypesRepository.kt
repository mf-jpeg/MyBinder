package pt.mf.mybinder.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.SubType
import pt.mf.mybinder.data.model.remote.SubTypesResponse
import retrofit2.Response

/**
 * Created by Martim Ferreira on 09/02/2025
 */
interface SubTypesRepository {
    suspend fun remoteFetchSubTypes(): Response<SubTypesResponse>
    suspend fun localFetchSubTypes(): Flow<List<SubType>>
    suspend fun insertSubType(subtype: SubType)
    suspend fun insertSubTypes(subtypes: List<SubType>)
}