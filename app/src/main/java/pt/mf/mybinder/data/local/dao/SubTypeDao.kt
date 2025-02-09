package pt.mf.mybinder.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.SubType

/**
 * Created by Martim Ferreira on 09/02/2025
 */
@Dao
interface SubTypeDao {
    @Upsert
    fun insertSubType(subtype: SubType)

    @Upsert
    fun insertSubTypes(subtypes: List<SubType>)

    @Query("SELECT * FROM SubType ORDER BY name ASC")
    fun getAll(): Flow<List<SubType>>
}