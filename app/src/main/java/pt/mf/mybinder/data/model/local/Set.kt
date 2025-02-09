package pt.mf.mybinder.data.model.local

import androidx.room.PrimaryKey

/**
 * Created by Martim Ferreira on 09/02/2025
 */
data class Set(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val total: Int,
    val printedTotal: Int,
    val releaseDate: String,
    val iconUrl: String
)