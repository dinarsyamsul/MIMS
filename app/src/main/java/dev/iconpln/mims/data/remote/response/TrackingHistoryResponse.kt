package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class TrackingHistoryResponse(

	@field:SerializedName("historis")
	val historis: List<HistorisItem>,

	@field:SerializedName("datas")
	val datas: List<DatasItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DatasItem(

	@field:SerializedName("serial_number")
	val serialNumber: String,

	@field:SerializedName("property_value")
	val propertyValue: String,

	@field:SerializedName("property_name")
	val propertyName: String
)

data class HistorisItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("stor_loc")
	val storLoc: Any,

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("material_group")
	val materialGroup: Any,

	@field:SerializedName("status_name")
	val statusName: String,

	@field:SerializedName("plant")
	val plant: Any,

	@field:SerializedName("serial_number")
	val serialNumber: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("nomor_transaksi")
	val nomorTransaksi: String
)

data class DetailTrackingHistoryResponse(

	@field:SerializedName("datas")
	val datas: DatasItemTracking,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DatasItemTracking(

	@field:SerializedName("MASA GARANSI")
	val mASAGARANSI: String? = null,

	@field:SerializedName("STORAGE LOCATION")
	val sTORAGELOCATION: Any? = null,

	@field:SerializedName("UNIT")
	val uNIT: Any? = null,

	@field:SerializedName("SPESIFIKASI MATERIAL")
	val sPESIFIKASIMATERIAL: String? = null,

	@field:SerializedName("KATEGORI MATERIAL")
	val kATEGORIMATERIAL: String? = null,

	@field:SerializedName("TANGGAL PRODUKSI")
	val tANGGALPRODUKSI: String? = null,

	@field:SerializedName("NOMOR MATERIAL")
	val nOMORMATERIAL: String? = null,

	@field:SerializedName("TIPE UPLOAD")
	val tIPEUPLOAD: String? = null,

	@field:SerializedName("TANGGAL UPLOAD")
	val tANGGALUPLOAD: String? = null,

	@field:SerializedName("SERIAL NUMBER")
	val sERIALNUMBER: String? = null,

	@field:SerializedName("NOMOR PRODUKSI")
	val nOMORPRODUKSI: String? = null,

	@field:SerializedName("NOMOR METROLOGI")
	val nOMORMETROLOGI: String? = null
)
