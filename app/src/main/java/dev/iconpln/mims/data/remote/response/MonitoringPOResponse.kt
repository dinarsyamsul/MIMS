package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class MonitoringPOResponse(

	@field:SerializedName("totalRow")
	val totalRow: Int,

	@field:SerializedName("data")
	val data: List<DataMonitoringPO>,

	@field:SerializedName("status")
	val status: String
)

data class DataMonitoringPO(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String,

	@field:SerializedName("mat_sap_no")
	val matSapNo: String,

	@field:SerializedName("stor_loc")
	val storLoc: String,

	@field:SerializedName("uom")
	val uom: String,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("material_desc")
	val materialDesc: String,

	@field:SerializedName("tlsk_no")
	val tlskNo: String,

	@field:SerializedName("po_sap_no")
	val poSapNo: String,

	@field:SerializedName("po_mp_no")
	val poMpNo: String,

	@field:SerializedName("qty")
	val qty: String,

	@field:SerializedName("lead_time")
	val leadTime: String,

	@field:SerializedName("created_date")
	val createdDate: String
)
