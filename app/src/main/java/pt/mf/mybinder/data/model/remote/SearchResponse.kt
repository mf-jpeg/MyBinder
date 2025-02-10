package pt.mf.mybinder.data.model.remote

import pt.mf.mybinder.domain.model.remote.Card

/**
 * Created by Martim Ferreira on 08/02/2025
 */
data class SearchResponse(
    val data: List<Card>,
    val page: Int,
    val pageSize: Int,
    val count: Int,
    val totalCount: Int
)