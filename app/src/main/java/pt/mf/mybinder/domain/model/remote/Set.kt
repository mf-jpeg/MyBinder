package pt.mf.mybinder.domain.model.remote

import com.google.gson.annotations.SerializedName

/**
 * Created by Martim Ferreira on 09/02/2025
 */
data class Set(
    val id: String,
    val name: String,
    val total: Int,
    val printedTotal: Int,
    val releaseDate: String,
    val images: Images
) {
    data class Images(
        @SerializedName("symbol") val icon: String
    )
}
