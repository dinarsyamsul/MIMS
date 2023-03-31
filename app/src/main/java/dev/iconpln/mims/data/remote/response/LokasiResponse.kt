package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class LokasiResponse(

	@field:SerializedName("datas")
	val datas: List<DatasItemLokasi>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DatasItemLokasi(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = null,

	@field:SerializedName("ket")
	val ket: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null
)
