package pt.mf.mybinder.domain.repository.remote

import pt.mf.mybinder.data.model.remote.SubTypesResponse
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
abstract class SubTypesRepository : BaseRepository() {
    abstract suspend fun fetchSubTypes(): Result<SubTypesResponse>
}