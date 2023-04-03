package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class AktivasiSerialNumberResponse(

	@field:SerializedName("data")
	val data: List<DataItemAktivasiSN>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("timestamp")
	val timestamp: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItemAktivasiSN(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String,

	@field:SerializedName("material_group")
	val materialGroup: String,

	@field:SerializedName("kode_gerak")
	val kodeGerak: String,

	@field:SerializedName("plant")
	val plant: String,

	@field:SerializedName("updated_by")
	val updatedBy: String,

	@field:SerializedName("serial_number")
	val serialNumber: String,

	@field:SerializedName("material_id")
	val materialId: String,

	@field:SerializedName("storloc")
	val storloc: String,

	@field:SerializedName("no_produksi")
	val noProduksi: String,

	@field:SerializedName("nomor_transaksi")
	val nomorTransaksi: String
)
