package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailSnResponse(

	@field:SerializedName("data")
	val detailSN: DetailSN,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DetailSN(

	@field:SerializedName("nama_pabrikan")
	val namaPabrikan: String,

	@field:SerializedName("nomor_material")
	val nomorMaterial: String,

	@field:SerializedName("spesifikasi_material")
	val spesifikasiMaterial: String,

	@field:SerializedName("tgl_produksi")
	val tglProduksi: String,

	@field:SerializedName("nomor_sert_metrologi")
	val nomorSertMetrologi: String,

	@field:SerializedName("serial_number")
	val serialNumber: String,

	@field:SerializedName("spln")
	val spln: String,

	@field:SerializedName("kode_pabrikan")
	val kodePabrikan: String,

	@field:SerializedName("masa_garansi")
	val masaGaransi: String,

	@field:SerializedName("no_produksi")
	val noProduksi: String,

	@field:SerializedName("no_packaging")
	val noPackaging: String,

	@field:SerializedName("kategori_material")
	val kategoriMaterial: String
)
