package dev.iconpln.mims.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.iconpln.mims.data.local.entity.MaterialEntity

@Database(entities = [MaterialEntity::class], version = 1, exportSchema = false)
abstract class MaterialDatabase : RoomDatabase() {
    abstract fun materialDao(): MaterialDao

    companion object{
        @Volatile
        private var instance: MaterialDatabase? = null
        fun getInstance(context: Context): MaterialDatabase =
            instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MaterialDatabase::class.java, "Material.db"
                ).build()
            }
    }
}