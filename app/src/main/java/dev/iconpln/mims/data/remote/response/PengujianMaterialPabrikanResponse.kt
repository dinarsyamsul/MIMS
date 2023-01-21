package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class PengujianMaterialPabrikanResponse(

	@field:SerializedName("totalRow")
	val totalRow: Int,

	@field:SerializedName("data")
	val data: List<DataItemPengujian>,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItemPengujian(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String,

	@field:SerializedName("qty_material")
	val qtyMaterial: String,

	@field:SerializedName("status_uji")
	val statusUji: String,

	@field:SerializedName("no_pengujian")
	val noPengujian: String,

	@field:SerializedName("qty_siap")
	val qtySiap: String,

	@field:SerializedName("flag_petugas")
	val flagPetugas: String,

	@field:SerializedName("tanggal_uji")
	val tanggalUji: String,

	@field:SerializedName("nama_kategori")
	val namaKategori: String
)
