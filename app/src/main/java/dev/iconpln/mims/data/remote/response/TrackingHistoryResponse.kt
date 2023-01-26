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
	val datas: List<DatasItemTracking>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DatasItemTracking(

	@field:SerializedName("no_khs")
	val noKhs: String,

	@field:SerializedName("usulan_koreksi")
	val usulanKoreksi: Int,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String,

	@field:SerializedName("serial_number")
	val serialNumber: String,

	@field:SerializedName("status_uji")
	val statusUji: String,

	@field:SerializedName("no_pengujian")
	val noPengujian: String,

	@field:SerializedName("flag_petugas")
	val flagPetugas: Int,

	@field:SerializedName("created_by")
	val createdBy: String,

	@field:SerializedName("alasan_reject")
	val alasanReject: String,

	@field:SerializedName("keterangan_material")
	val keteranganMaterial: String,

	@field:SerializedName("pengujian_ke")
	val pengujianKe: Int,

	@field:SerializedName("link_pengajuan_ust")
	val linkPengajuanUst: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("create_date")
	val createDate: String,

	@field:SerializedName("tanggal_pengajuan")
	val tanggalPengajuan: String,

	@field:SerializedName("tanggal_uji")
	val tanggalUji: String,

	@field:SerializedName("tanggal_ust")
	val tanggalUst: Any
)
