package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class MonitoringAktivasiMaterialResponse(

	@field:SerializedName("totalRow")
	val totalRow: Int,

	@field:SerializedName("data")
	val data: List<DataItemRegisSn>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItemRegisSn(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String,

	@field:SerializedName("material_desc")
	val materialDesc: String,

	@field:SerializedName("tahun")
	val tahun: String,

	@field:SerializedName("tgl_registrasi")
	val tglRegistrasi: String,

	@field:SerializedName("vendor")
	val vendor: String,

	@field:SerializedName("serial_number")
	val serialNumber: String,

	@field:SerializedName("flag_print")
	val flagPrint: Boolean,

	@field:SerializedName("no_produksi")
	val noProduksi: String
)
