package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailSnResponse(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("data")
    val data: List<DetailSN>
)

data class DetailSN(

    @field:SerializedName("nama_pabrikan")
    val namaPabrikan: String,

    @field:SerializedName("nama_klasifikasi_retur")
    val namaKlasifikasiRetur: String,

    @field:SerializedName("material_desc")
    val materialDesc: String,

    @field:SerializedName("tahun")
    val tahun: String,

    @field:SerializedName("serial_number")
    val serialNumber: String,

    @field:SerializedName("no_asset")
    val noAsset: String,

    @field:SerializedName("no_ba")
    val noBa: String,

    @field:SerializedName("tgl_inspeksiretur")
    val tglInspeksiretur: String,

    @field:SerializedName("nama_inspektur")
    val namaInspektur: String
)
