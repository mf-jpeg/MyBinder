package pt.mf.mybinder.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Martim Ferreira on 07/02/2025
 */
data class Card(
    val id: String,
    val name: String,
    @SerializedName("supertype") val superType: String,
    @SerializedName("subtypes") val subTypes: List<String>,
    val hp: String,
    val types: List<String>,
    val set: Set,
    val number: String,
    val images: Images,
    @SerializedName("cardmarket") val cardMarket: CardMarket?
) {
    data class Set(
        val id: String,
        val name: String,
        val series: String,
        val releaseDate: String,
        val images: SetImages
    ) {
        data class SetImages(
            val symbol: String,
            val logo: String
        )
    }

    data class Images(
        val small: String,
        val large: String
    )

    data class CardMarket(
        val url: String,
        val updatedAt: String,
        val prices: Prices
    ) {
        data class Prices(
            val averageSellPrice: Float,
            val lowPrice: Float,
            val trendPrice: Float
        )
    }
}