package pt.mf.mybinder.data.model

import pt.mf.mybinder.domain.model.Card

/**
 * Created by Martim Ferreira on 08/02/2025
 */
data class CardSearchResponse(
    val data: List<Card>,
    val page: Int,
    val pageSize: Int,
    val count: Int,
    val totalCount: Int
)