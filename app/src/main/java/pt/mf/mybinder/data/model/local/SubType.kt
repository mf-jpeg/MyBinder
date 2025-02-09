package pt.mf.mybinder.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Martim Ferreira on 09/02/2025
 */
@Entity
data class Subtype(
    @PrimaryKey(autoGenerate = false) val name: String,
)
