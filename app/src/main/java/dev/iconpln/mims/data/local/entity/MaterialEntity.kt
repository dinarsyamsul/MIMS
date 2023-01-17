package dev.iconpln.mims.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_material")
class MaterialEntity(
    @field:ColumnInfo(name = "no_material")
    @field:PrimaryKey
    val noMaterial: String,

    @field:ColumnInfo(name = "serial_number")
    val serialNumber: String,

    @field:ColumnInfo(name = "nama_pabrikan")
    val namaPabrikan: String

) : Parcelable