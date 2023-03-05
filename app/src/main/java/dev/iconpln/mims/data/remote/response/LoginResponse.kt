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

	@field:SerializedName("pengujians")
	val pengujians: List<PengujiansItem?>? = null,

	@field:SerializedName("pos")
	val pos: List<PosItem?>? = null,

	@field:SerializedName("pengujian_details")
	val pengujianDetails: List<PengujianDetailsItem?>? = null,

	@field:SerializedName("materials")
	val materials: List<MaterialsItem?>? = null,

	@field:SerializedName("scope")
	val scope: String? = null,

	@field:SerializedName("lokasis")
	val lokasis: List<LokasisItem?>? = null,

	@field:SerializedName("material_details")
	val materialDetails: List<MaterialDetailsItem?>? = null,

	@field:SerializedName("ratings")
	val ratings: List<RatingsItem?>? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("monitoring_permintaan")
	val monitoringPermintaan: List<MonitoringPermintaanItem?>? = null,

	@field:SerializedName("monitoring_permintaan_details")
	val monitoringPermintaanDetails: List<MonitoringPermintaanDetailsItem?>? = null
)

data class RatingsItem(

	@field:SerializedName("kd_rating")
	val kdRating: String? = "",

	@field:SerializedName("keterangan")
	val keterangan: String? = "",

	@field:SerializedName("nilai")
	val nilai: Int? = 0

)

data class PosDetailItem(

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = "",

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = "",

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = "",

	@field:SerializedName("no_packaging")
	val noPackaging: String? = "",

	@field:SerializedName("plant_code_no")
	val plantCodeNo: String? = "",

	@field:SerializedName("stor_loc")
	val storLoc: String? = "",

	@field:SerializedName("uom")
	val uom: String? = "",

	@field:SerializedName("no_pemeriksaan")
	val noPemeriksaan: String? = "",

	@field:SerializedName("po_sap_no")
	val poSapNo: String? = "",

	@field:SerializedName("po_mp_no")
	val poMpNo: String? = "",

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = "",

	@field:SerializedName("qty")
	val qty: String? = "",

	@field:SerializedName("lead_time")
	val leadTime: Int? = 0,

	@field:SerializedName("created_date")
	val createdDate: String? = "",

	@field:SerializedName("do_status")
	val doStatus: String? = "",

	@field:SerializedName("plant_name")
	val plantName: String? = ""
)

data class PengujianDetailsItem(

	@field:SerializedName("keterangan_material")
	val keteranganMaterial: String? = null,

	@field:SerializedName("serial_number")
	val serialNumber: String? = null,

	@field:SerializedName("status_uji")
	val statusUji: String? = null,

	@field:SerializedName("no_pengujian")
	val noPengujian: String? = null,

	@field:SerializedName("nama_kategori")
	val namaKategori: String? = null
)

data class PosSnsItem(

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = "",

	@field:SerializedName("mmc")
	val mmc: String? = "",

	@field:SerializedName("material_group")
	val materialGroup: String? = "",

	@field:SerializedName("tgl_produksi")
	val tglProduksi: String? = "",

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = "",

	@field:SerializedName("nomor_sert_materologi")
	val nomorSertMaterologi: String? = "",

	@field:SerializedName("spln")
	val spln: String? = "",

	@field:SerializedName("no_produksi")
	val noProduksi: String? = "",

	@field:SerializedName("storloc")
	val storloc: String? = "",

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = "",

	@field:SerializedName("no_packaging")
	val noPackaging: String? = "",

	@field:SerializedName("no_serial")
	val noSerial: String? = "",

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = "",

	@field:SerializedName("spesifikasi")
	val spesifikasi: String? = "",

	@field:SerializedName("plant")
	val plant: String? = "",

	@field:SerializedName("material_id")
	val materialId: String? = "",

	@field:SerializedName("masa_garansi")
	val masaGaransi: String? = "",

	@field:SerializedName("do_status")
	val doStatus: String? = "",

	@field:SerializedName("do_line_item")
	val doLineItem: String? = "",

	@field:SerializedName("status")
	val status: String? = ""
)

data class MaterialsItem(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = "",

	@field:SerializedName("mmc")
	val mmc: String? = "",

	@field:SerializedName("material_group")
	val materialGroup: String? = "",

	@field:SerializedName("tahun")
	val tahun: Int? = 0,

	@field:SerializedName("tgl_produksi")
	val tglProduksi: String? = "",

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = "",

	@field:SerializedName("nomor_sert_materologi")
	val nomorSertMaterologi: String? = "",

	@field:SerializedName("no_produksi")
	val noProduksi: String? = "",

	@field:SerializedName("storloc")
	val storloc: String? = "",

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = "",

	@field:SerializedName("plant")
	val plant: String? = "",

	@field:SerializedName("material_id")
	val materialId: String? = "",

	@field:SerializedName("masa_garansi")
	val masaGaransi: String? = ""
)

data class PosItem(

	@field:SerializedName("material_group")
	val materialGroup: String? = "",

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = "",

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = "",

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = "",

	@field:SerializedName("plant_code_no")
	val plantCodeNo: String? = "",

	@field:SerializedName("stor_loc")
	val storLoc: String? = "",

	@field:SerializedName("total")
	val total: String? = "",

	@field:SerializedName("tlsk_no")
	val tlskNo: String? = "",

	@field:SerializedName("po_sap_no")
	val poSapNo: String? = "",

	@field:SerializedName("po_mp_no")
	val poMpNo: String? = "",

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = "",

	@field:SerializedName("lead_time")
	val leadTime: Int? = 0,

	@field:SerializedName("created_date")
	val createdDate: String? = "",

	@field:SerializedName("courier_person_name")
	val courierPersonName: String? = "",

	@field:SerializedName("do_status")
	val doStatus: String? = "",

	@field:SerializedName("ekspedition")
	val ekspedition: String? = "",

	@field:SerializedName("kode_status_do_mims")
	val kodeStatusDoMims: String? = "",

	@field:SerializedName("do_line_item")
	val DoLineItem: String? = "",

	@field:SerializedName("petugas_penerima")
	val PetugasPenerima: String? = "",

	@field:SerializedName("tgl_serah_terima")
	val TglSerahTerima: String? = "",

	@field:SerializedName("kurir_pengirim")
	val KurirPengirim: String? = "",

	@field:SerializedName("plant_name")
	val plantName: String? = ""
)

data class PengujiansItem(

	@field:SerializedName("tanggal_uji")
	val tglUji: String? = null,

	@field:SerializedName("no_pengujian")
	val noPengujian: String? = null,

	@field:SerializedName("nama_kategori")
	val namaKategori: String? = null,

	@field:SerializedName("qty_material")
	val qtyMaterial: String? = null,

	@field:SerializedName("qty_lolos")
	val qtyLolos: String? = null,

	@field:SerializedName("qty_tdk_lolos")
	val qtyTdkLolos: String? = null,

	@field:SerializedName("qty_rusak")
	val qtyRusak: Any? = null,

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("status_uji")
	val statusUji: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null

)

data class MaterialGroupsItem(

	@field:SerializedName("material_group")
	val materialGroup: String? = "",

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = ""
)

data class MaterialDetailsItem(

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = "",

	@field:SerializedName("mmc")
	val mmc: String? = "",

	@field:SerializedName("material_group")
	val materialGroup: String? = "",

	@field:SerializedName("tahun")
	val tahun: Int? = 0,

	@field:SerializedName("tgl_produksi")
	val tglProduksi: String? = "",

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = "",

	@field:SerializedName("serial_number")
	val serialNumber: String? = "",

	@field:SerializedName("nomor_sert_materologi")
	val nomorSertMaterologi: String? = "",

	@field:SerializedName("spln")
	val spln: String? = "",

	@field:SerializedName("no_produksi")
	val noProduksi: String? = "",

	@field:SerializedName("storloc")
	val storloc: String? = "",

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = "",

	@field:SerializedName("no_packaging")
	val noPackaging: String? = "",

	@field:SerializedName("spesifikasi")
	val spesifikasi: String? = "",

	@field:SerializedName("plant")
	val plant: String? = "",

	@field:SerializedName("material_id")
	val materialId: String? = "",

	@field:SerializedName("masa_garansi")
	val masaGaransi: String? = ""
)

data class User(

	@field:SerializedName("kd_user")
	val kdUser: String? = "",

	@field:SerializedName("mail")
	val mail: String? = "",

	@field:SerializedName("user_id")
	val userId: Int? = 0,

	@field:SerializedName("role_id")
	val roleId: Int? = 0
)

data class PrivilegeItem(

	@field:SerializedName("module_id")
	val moduleId: String? = "",

	@field:SerializedName("is_active")
	val isActive: Int? = 0,

	@field:SerializedName("role_id")
	val roleId: Int? = 0,

	@field:SerializedName("method_id")
	val methodId: String? = "",

	@field:SerializedName("method_value")
	val methodValue: String? = ""
)

data class LokasisItem(

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = "",

	@field:SerializedName("ket")
	val ket: String? = "",

	@field:SerializedName("updated_date")
	val updatedDate: String? = ""
)

data class MonitoringPermintaanItem(

	@field:SerializedName("stor_loc_tujuan_name")
	val storLocTujuanName: String? = null,

	@field:SerializedName("kode_pengeluaran")
	val kodePengeluaran: Int? = null,

	@field:SerializedName("stor_loc_tujuan")
	val storLocTujuan: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("no_repackaging")
	val noRepackaging: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null,

	@field:SerializedName("no_permintaan")
	val noPermintaan: String? = null,

	@field:SerializedName("jumlah_kardus")
	val jumlahKardus: Any? = null,

	@field:SerializedName("stor_loc_asal_name")
	val storLocAsalName: String? = null,

	@field:SerializedName("tanggal_permintaan")
	val tanggalPermintaan: String? = null,

	@field:SerializedName("tanggal_pengeluaran")
	val tanggalPengeluaran: Any? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null,

	@field:SerializedName("stor_loc_asal")
	val storLocAsal: String? = null
)

data class MonitoringPermintaanDetailsItem(

	@field:SerializedName("no_repackaging")
	val noRepackaging: String? = null,

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("qty_permintaan")
	val qtyPermintaan: Int? = null,

	@field:SerializedName("material_desc")
	val materialDesc: String? = null,

	@field:SerializedName("qty_scan")
	val qtyScan: Int? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("no_permintaan")
	val noPermintaan: String? = null,

	@field:SerializedName("qty_pengeluaran")
	val qtyPengeluaran: Any? = null
)
