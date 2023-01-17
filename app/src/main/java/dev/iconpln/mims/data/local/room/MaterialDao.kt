package dev.iconpln.mims.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.iconpln.mims.data.local.entity.MaterialEntity

@Dao
interface MaterialDao {
    @Query("SELECT * FROM tbl_material")
    fun getSavedMaterial(): LiveData<List<MaterialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMaterial(material: MaterialEntity)

    @Query("DELETE FROM tbl_material WHERE no_material = :noMaterial")
    suspend fun deleteMaterial(noMaterial: String)
}