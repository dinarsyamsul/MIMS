package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetMaterialAktivasiResponse(

	@field:SerializedName("totalRow")
	val totalRow: Int,

	@field:SerializedName("data")
	val data: List<DataItemMaterialAktivasi>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItemMaterialAktivasi(

	@field:SerializedName("total_sn")
	val totalSn: Int,

	@field:SerializedName("total_sn_process")
	val totalSnProcess: Int,

	@field:SerializedName("total_sn_approve")
	val totalSnApprove: Int,

	@field:SerializedName("tgl_registrasi")
	val tglRegistrasi: String,

	@field:SerializedName("status")
	val status: String
)
