package pt.mf.mybinder.data.model.remote

import pt.mf.mybinder.domain.model.remote.Set

/**
 * Created by Martim Ferreira on 09/02/2025
 */
data class SetResponse(
    val data: List<Set>,
    val page: Int,
    val pageSize: Int,
    val count: Int,
    val totalCountL: Int
)
