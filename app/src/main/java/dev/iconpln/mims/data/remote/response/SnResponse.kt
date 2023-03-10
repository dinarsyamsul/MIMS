package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class SnResponse(

	@field:SerializedName("no_material")
	val noMaterial: String? = null,

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = null,

	@field:SerializedName("serial_number")
	val serialNumber: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
