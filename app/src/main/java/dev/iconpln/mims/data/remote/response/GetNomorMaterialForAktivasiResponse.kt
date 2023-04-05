package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetNomorMaterialForAktivasiResponse(

	@field:SerializedName("totalRow")
	val totalRow: Int,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItem(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String,

	@field:SerializedName("material_desc")
	val materialDesc: String
)
