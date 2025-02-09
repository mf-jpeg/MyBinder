package pt.mf.mybinder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.Subtype

/**
 * Created by Martim Ferreira on 09/02/2025
 */
@Dao
interface SubtypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSubType(subtype: Subtype)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSubTypes(subtypes: List<Subtype>)

    @Query("SELECT * FROM Subtype ORDER BY name ASC")
    fun getAll(): Flow<List<Subtype>>
}