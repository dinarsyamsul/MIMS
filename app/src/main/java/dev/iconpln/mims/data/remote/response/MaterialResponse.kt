package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class MaterialResponse(

	@field:SerializedName("totalRow")
	val totalRow: Int,

	@field:SerializedName("data")
	val data: List<DataItemMaterial>,

	@field:SerializedName("status")
	val status: String
)

data class DataItemMaterial(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String,

	@field:SerializedName("tahun_produksi")
	val tahunProduksi: String,

	@field:SerializedName("source")
	val source: String,

	@field:SerializedName("no_produksi")
	val noProduksi: String,

	@field:SerializedName("kategori_material")
	val kategoriMaterial: String
)
