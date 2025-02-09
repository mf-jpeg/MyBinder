package pt.mf.mybinder.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pt.mf.mybinder.data.model.local.Set

/**
 * Created by Martim Ferreira on 09/02/2025
 */
@Dao
interface SetDao {
    @Upsert
    fun insertSet(set: Set)

    @Upsert
    fun insertSets(sets: List<Set>)

    @Query("SELECT * FROM `set` ORDER BY releaseDate ASC")
    fun getAll(): Flow<List<Set>>

    @Query("SELECT * FROM `set` WHERE id=:id LIMIT 1")
    fun getById(id: String): Flow<Set?>
}