package pt.mf.mybinder.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.mf.mybinder.data.local.dao.SetDao
import pt.mf.mybinder.data.local.dao.SubtypeDao
import pt.mf.mybinder.data.model.local.Set
import pt.mf.mybinder.data.model.local.Subtype
import pt.mf.mybinder.utils.MyBinder

/**
 * Created by Martim Ferreira on 09/02/2025
 */
@Database(
    entities = [Set::class, Subtype::class],
    version = 1
)
abstract class MyBinderDatabase : RoomDatabase() {
    abstract val setDao: SetDao
    abstract val subTypeDao: SubtypeDao

    private object HOLDER {
        val INSTANCE = Room.databaseBuilder(
            MyBinder.ctx, MyBinderDatabase::class.java, DATABASE
        ).build()
    }

    companion object {
        private const val DATABASE = "MyBinderDatabase"
        val instance: MyBinderDatabase by lazy { HOLDER.INSTANCE }
    }
}