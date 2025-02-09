package pt.mf.mybinder.domain.repository.remote

import pt.mf.mybinder.data.model.remote.SetResponse
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 09/02/2025
 */
abstract class SetRepository : BaseRepository() {
    abstract suspend fun fetchSets(): Result<SetResponse>
}