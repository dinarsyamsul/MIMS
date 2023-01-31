package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("pos_sns")
	val posSns: List<PosSnsItem?>? = null,

	@field:SerializedName("privilege")
	val privilege: List<PrivilegeItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("material_groups")
	val materialGroups: List<MaterialGroupsItem?>? = null,

	@field:SerializedName("pos_detail")
	val posDetail: List<PosDetailItem?>? = null,

//	@field:SerializedName("pengujians")
//	val pengujians: List<PengujiansItem?>? = null,

	@field:SerializedName("pos")
	val pos: List<PosItem?>? = null,

//	@field:SerializedName("pengujian_details")
//	val pengujianDetails: List<PengujianDetailsItem?>? = null,

	@field:SerializedName("materials")
	val materials: List<MaterialsItem?>? = null,

	@field:SerializedName("scope")
	val scope: String? = null,

	@field:SerializedName("lokasis")
	val lokasis: List<LokasisItem?>? = null,

	@field:SerializedName("material_details")
	val materialDetails: List<MaterialDetailsItem?>? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PosDetailItem(

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null,

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = null,

	@field:SerializedName("no_packaging")
	val noPackaging: String? = null,

	@field:SerializedName("plant_code_no")
	val plantCodeNo: String? = null,

	@field:SerializedName("stor_loc")
	val storLoc: String? = null,

	@field:SerializedName("uom")
	val uom: String? = null,

	@field:SerializedName("no_pemeriksaan")
	val noPemeriksaan: Any? = null,

	@field:SerializedName("po_sap_no")
	val poSapNo: String? = null,

	@field:SerializedName("po_mp_no")
	val poMpNo: String? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("lead_time")
	val leadTime: Int? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("do_status")
	val doStatus: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null
)

//data class PengujianDetailsItem(
//
//	@field:SerializedName("keterangan_material")
//	val keteranganMaterial: String? = null,
//
//	@field:SerializedName("serial_number")
//	val serialNumber: String? = null,
//
//	@field:SerializedName("status_uji")
//	val statusUji: String? = null,
//
//	@field:SerializedName("no_pengujian")
//	val noPengujian: String? = null,
//
//	@field:SerializedName("nama_kategori")
//	val namaKategori: String? = null
//)

data class PosSnsItem(

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = null,

	@field:SerializedName("mmc")
	val mmc: String? = null,

	@field:SerializedName("material_group")
	val materialGroup: String? = null,

	@field:SerializedName("tgl_produksi")
	val tglProduksi: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null,

	@field:SerializedName("nomor_sert_materologi")
	val nomorSertMaterologi: String? = null,

	@field:SerializedName("spln")
	val spln: String? = null,

	@field:SerializedName("no_produksi")
	val noProduksi: String? = null,

	@field:SerializedName("storloc")
	val storloc: String? = null,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = null,

	@field:SerializedName("no_packaging")
	val noPackaging: String? = null,

	@field:SerializedName("no_serial")
	val noSerial: String? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("spesifikasi")
	val spesifikasi: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("material_id")
	val materialId: String? = null,

	@field:SerializedName("masa_garansi")
	val masaGaransi: String? = null,

	@field:SerializedName("do_status")
	val doStatus: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class MaterialsItem(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("mmc")
	val mmc: String? = null,

	@field:SerializedName("material_group")
	val materialGroup: String? = null,

	@field:SerializedName("tahun")
	val tahun: Int? = null,

	@field:SerializedName("tgl_produksi")
	val tglProduksi: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null,

	@field:SerializedName("nomor_sert_materologi")
	val nomorSertMaterologi: String? = null,

	@field:SerializedName("no_produksi")
	val noProduksi: String? = null,

	@field:SerializedName("storloc")
	val storloc: String? = null,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("material_id")
	val materialId: String? = null,

	@field:SerializedName("masa_garansi")
	val masaGaransi: String? = null
)

data class PosItem(

	@field:SerializedName("material_group")
	val materialGroup: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null,

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = null,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = null,

	@field:SerializedName("plant_code_no")
	val plantCodeNo: String? = null,

	@field:SerializedName("stor_loc")
	val storLoc: String? = null,

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("tlsk_no")
	val tlskNo: String? = null,

	@field:SerializedName("po_sap_no")
	val poSapNo: String? = null,

	@field:SerializedName("po_mp_no")
	val poMpNo: String? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("lead_time")
	val leadTime: Int? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("courier_person_name")
	val courierPersonName: String? = null,

	@field:SerializedName("do_status")
	val doStatus: String? = null,

	@field:SerializedName("ekspedition")
	val ekspedition: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null
)

//data class PengujiansItem(
//
//	@field:SerializedName("unit")
//	val unit: String? = null,
//
//	@field:SerializedName("kd_pabrikan")
//	val kdPabrikan: String? = null,
//
//	@field:SerializedName("qty_material")
//	val qtyMaterial: String? = null,
//
//	@field:SerializedName("status_uji")
//	val statusUji: String? = null,
//
//	@field:SerializedName("no_pengujian")
//	val noPengujian: String? = null,
//
//	@field:SerializedName("qty_siap")
//	val qtySiap: String? = null,
//
//	@field:SerializedName("tanggal_uji")
//	val tanggalUji: Any? = null,
//
//	@field:SerializedName("nama_kategori")
//	val namaKategori: String? = null
//)

data class MaterialGroupsItem(

	@field:SerializedName("material_group")
	val materialGroup: String? = null,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = null
)

data class MaterialDetailsItem(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("mmc")
	val mmc: String? = null,

	@field:SerializedName("material_group")
	val materialGroup: String? = null,

	@field:SerializedName("tahun")
	val tahun: Int? = null,

	@field:SerializedName("tgl_produksi")
	val tglProduksi: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null,

	@field:SerializedName("serial_number")
	val serialNumber: String? = null,

	@field:SerializedName("nomor_sert_materologi")
	val nomorSertMaterologi: String? = null,

	@field:SerializedName("spln")
	val spln: String? = null,

	@field:SerializedName("no_produksi")
	val noProduksi: String? = null,

	@field:SerializedName("storloc")
	val storloc: String? = null,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = null,

	@field:SerializedName("no_packaging")
	val noPackaging: String? = null,

	@field:SerializedName("spesifikasi")
	val spesifikasi: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("material_id")
	val materialId: String? = null,

	@field:SerializedName("masa_garansi")
	val masaGaransi: String? = null
)

data class User(

	@field:SerializedName("kd_user")
	val kdUser: String? = null,

	@field:SerializedName("mail")
	val mail: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("role_id")
	val roleId: Int? = null
)

data class PrivilegeItem(

	@field:SerializedName("module_id")
	val moduleId: String? = null,

	@field:SerializedName("is_active")
	val isActive: Int? = null,

	@field:SerializedName("role_id")
	val roleId: Int? = null,

	@field:SerializedName("method_id")
	val methodId: String? = null,

	@field:SerializedName("method_value")
	val methodValue: String? = null
)

data class LokasisItem(

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = null,

	@field:SerializedName("ket")
	val ket: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null
)
