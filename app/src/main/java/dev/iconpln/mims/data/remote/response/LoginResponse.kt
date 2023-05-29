package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("pemeriksaanDetail")
	val pemeriksaanDetail: List<PemeriksaanDetailItem?>? = null,

	@field:SerializedName("pemeriksan")
	val pemeriksan: List<PemeriksanItem?>? = null,

	@field:SerializedName("pos_sns")
	val posSns: List<PosSnsItem?>? = null,

	@field:SerializedName("privilege")
	val privilege: List<PrivilegeItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("id_token_sso")
	val idTokenSso: String? = null,

	@field:SerializedName("webtoken")
	val webToken: String? = null,

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

	@field:SerializedName("dataRatings")
	val dataRatings: List<DataRatingsItem?>? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("monitoring_permintaan")
	val monitoringPermintaan: List<MonitoringPermintaanItem?>? = null,

	@field:SerializedName("monitoring_permintaan_details")
	val monitoringPermintaanDetails: List<MonitoringPermintaanDetailsItem?>? = null,

	@field:SerializedName("sn_permaterial")
	val snPermaterial: List<SnPermaterialItem?>? = null,

	@field:SerializedName("sn_permintaan")
	val snPermintaan: List<SnPermintaanItem?>? = null,

	@field:SerializedName("penerimaanUlp")
	val penerimaanUlp: List<PenerimaanUlpItem?>? = null,

	@field:SerializedName("penerimaanDetailUlp")
	val penerimaanDetailUlp: List<PenerimaanDetailUlpItem?>? = null,

	@field:SerializedName("pemakaian")
	val pemakaian: List<PemakaianItem?>? = null,

	@field:SerializedName("pemakaianDetail")
	val pemakaianDetail: List<PemakaianDetailItem?>? = null,

	@field:SerializedName("snPemakaianUlp")
	val snPemakaianUlp: List<SnPemakaianUlpItem?>? = null,

	@field:SerializedName("snPenerimaanUlp")
	val snPenerimaanUlp: List<SnPenerimaanUlpItem?>? = null,

	@field:SerializedName("petugas_pengujian")
	val petugasPengujian: List<PetugasPengujianItem?>? = null,

	@field:SerializedName("monitoring_komplain")
	val monitoringKomplain: List<MonitoringKomplainItem?>? = null,

	@field:SerializedName("monitoring_komplain_detail")
	val monitoringKomplainDetail: List<MonitoringKomplainDetailItem?>? = null,

	@field:SerializedName("pegawai")
	val pegawai: List<PegawaiItem?>? = null,

	@field:SerializedName("data_penerimaan_akhir")
	val dataPenerimaanAkhir: List<DataPenerimaanAkhirItem?>? = null
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

	@field:SerializedName("status_penerimaan")
	val statusPenerimaan: String? = "",

	@field:SerializedName("status_pemeriksaan")
	val statusPemeriksaan: String? = ""
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

	@field:SerializedName("po_date")
	val poDate: String? = "",

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
	val plantName: String? = "",

	@field:SerializedName("rating_delivery")
	val ratingDelivery: String? = "",

	@field:SerializedName("rating_response")
	val ratingResponse: String? = "",

	@field:SerializedName("eta")
	val eta: String? = "",

	@field:SerializedName("etd")
	val etd: String? = "",

	@field:SerializedName("rating_quality")
	val ratingQuality: String? = "",

	@field:SerializedName("status_pemeriksaan")
	val statusPemeriksaan: String? = "",

	@field:SerializedName("status_penerimaan")
	val statusPenerimaan: String? = "",

	@field:SerializedName("sudah_bisa_rating")
	val sudahBisaRating: Boolean,

	@field:SerializedName("isbabg")
	val isBabg: Boolean,

	@field:SerializedName("isbabgconfirm")
	val isBabgConfirm: Boolean,

	@field:SerializedName("sla_integrasi_sap")
	val slaIntegrasiSap: Boolean,

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
	val qtyRusak: Int? = null,

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("status_uji")
	val statusUji: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null,

	@field:SerializedName("tanggal_usul_uji")
	val tanggalUsulUji: String? = null

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

	@field:SerializedName("plant")
	val plant: String? = "",

	@field:SerializedName("plant_name")
	val plantName: String? = "",

	@field:SerializedName("stor_loc")
	val storloc: String? = "",

	@field:SerializedName("stor_loc_name")
	val storLocName: String? = "",

	@field:SerializedName("mail")
	val mail: String? = "",

	@field:SerializedName("user_id")
	val userId: Int? = 0,

	@field:SerializedName("role_id")
	val roleId: Int? = 0,

	@field:SerializedName("subrole_id")
	val subroleId: Int? = 0,

	@field:SerializedName("user_name")
	val userName: String? = "",

	@field:SerializedName("full_name")
	val fullName: String? = "",

	@field:SerializedName("role_name")
	val roleName: String? = ""
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
	@field:SerializedName("id")
	val id: String? = "",

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
	val jumlahKardus: Int? = null,

	@field:SerializedName("stor_loc_asal_name")
	val storLocAsalName: String? = null,

	@field:SerializedName("tanggal_permintaan")
	val tanggalPermintaan: String? = null,

	@field:SerializedName("tanggal_pengeluaran")
	val tanggalPengeluaran: Any? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null,

	@field:SerializedName("stor_loc_asal")
	val storLocAsal: String? = null,

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = null,
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
	val qtyPengeluaran: Any? = null,

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = null,
)

data class SnPermaterialItem(

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

	@field:SerializedName("do_line_item")
	val doLineItem: String? = null,

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

data class PemeriksanItem(

	@field:SerializedName("tgl_serah_terima")
	val tglSerahTerima: String? = null,

	@field:SerializedName("petugas_penerima")
	val petugasPenerima: String? = null,

	@field:SerializedName("kurir_pengirim")
	val kurirPengirim: String? = null,

	@field:SerializedName("rating_response")
	val ratingResponse: Any? = null,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = null,

	@field:SerializedName("no_serial")
	val noSerial: String? = null,

	@field:SerializedName("plant_code_no")
	val plantCodeNo: String? = null,

	@field:SerializedName("rating_delivery")
	val ratingDelivery: Any? = null,

	@field:SerializedName("ketua_pemeriksa")
	val ketuaPemeriksa: String? = null,

	@field:SerializedName("stor_loc")
	val storLoc: String? = null,

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("lead_time")
	val leadTime: Int? = null,

	@field:SerializedName("kode_status_do_mims")
	val kodeStatusDoMims: String? = null,

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = null,

	@field:SerializedName("material_group")
	val materialGroup: String? = null,

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = null,

	@field:SerializedName("no_do_mims")
	val noDoMims: String? = null,

	@field:SerializedName("no_packaging")
	val noPackaging: String? = null,

	@field:SerializedName("rating_quality")
	val ratingQuality: Any? = null,

	@field:SerializedName("status_pemeriksaan")
	val statusPemeriksaan: String? = null,

	@field:SerializedName("no_pemeriksaan")
	val noPemeriksaan: String? = null,

	@field:SerializedName("tlsk_no")
	val tlskNo: String? = null,

	@field:SerializedName("po_sap_no")
	val poSapNo: String? = null,

	@field:SerializedName("po_mp_no")
	val poMpNo: String? = null,

	@field:SerializedName("do_line_item")
	val doLineItem: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("courier_person_name")
	val courierPersonName: String? = null,

	@field:SerializedName("do_status")
	val doStatus: String? = null,

	@field:SerializedName("ekspedition")
	val ekspedition: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PemeriksaanDetailItem(

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = null,

	@field:SerializedName("no_pemeriksaan")
	val noPemeriksaan: String? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = null,

	@field:SerializedName("no_packaging")
	val noPackaging: String? = null,

	@field:SerializedName("no_serial")
	val noSerial: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class SnPermintaanItem(

	@field:SerializedName("no_repackaging")
	val noRepackaging: String? = null,

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("serial_number")
	val serialNumber: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PenerimaanDetailUlpItem(

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = null,

	@field:SerializedName("no_repackaging")
	val noRepackaging: String? = null,

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("qty_permintaan")
	val qtyPermintaan: Int? = null,

	@field:SerializedName("qty_pengiriman")
	val qtyPengiriman: Int? = null,

	@field:SerializedName("material_desc")
	val materialDesc: String? = null,

	@field:SerializedName("qty_penerimaan")
	val qtyPenerimaan: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("qty_pemeriksaan")
	val qtyPemeriksaan: Int? = null,

	@field:SerializedName("qty_sesuai")
	val qtySesuai: Int? = null
)

data class PenerimaanUlpItem(

	@field:SerializedName("status_penerimaan")
	val statusPenerimaan: String? = null,

	@field:SerializedName("stor_loc_tujuan_name")
	val storLocTujuanName: String? = null,

	@field:SerializedName("pejabat_penerima")
	val pejabatPenerima: String? = null,

	@field:SerializedName("jabatan_pemeriksa_2")
	val jabatanPemeriksa2: String? = null,

	@field:SerializedName("qty_sesuai")
	val qtySesuai: Int? = null,

	@field:SerializedName("no_penerimaan")
	val noPenerimaan: String? = null,

	@field:SerializedName("tanggal_penerimaan")
	val tanggalPenerimaan: String? = null,

	@field:SerializedName("jabatan_pemeriksa_1")
	val jabatanPemeriksa1: String? = null,

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = null,

	@field:SerializedName("qty_pengiriman")
	val qtyPengiriman: Int? = null,

	@field:SerializedName("material_desc")
	val materialDesc: String? = null,

	@field:SerializedName("qty_penerimaan")
	val qtyPenerimaan: Int? = null,

	@field:SerializedName("tanggal_dokumen")
	val tanggalDokumen: String? = null,

	@field:SerializedName("qty_pemeriksaan")
	val qtyPemeriksaan: Int? = null,

	@field:SerializedName("tanggal_pengiriman")
	val tanggalPengiriman: String? = null,

	@field:SerializedName("jumlah_kardus")
	val jumlahKardus: Int? = null,

	@field:SerializedName("stor_loc_asal")
	val storLocAsal: String? = null,

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("qty_permintaan")
	val qtyPermintaan: Int? = null,

	@field:SerializedName("stor_loc_tujuan")
	val storLocTujuan: String? = null,

	@field:SerializedName("no_nota")
	val noNota: String? = null,

	@field:SerializedName("kurir")
	val kurir: String? = null,

	@field:SerializedName("status_kirim_ago")
	val statusKirimAgo: Any? = null,

	@field:SerializedName("nama_pemeriksa_2")
	val namaPemeriksa2: String? = null,

	@field:SerializedName("nama_pemeriksa_1")
	val namaPemeriksa1: String? = null,

	@field:SerializedName("no_repackaging")
	val noRepackaging: String? = null,

	@field:SerializedName("no_pemeriksaan")
	val noPemeriksaan: String? = null,

	@field:SerializedName("status_pemeriksaan")
	val statusPemeriksaan: String? = null,

	@field:SerializedName("tanggal_pemeriksaan")
	val tanggalPemeriksaan: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("no_pk")
	val noPk: String? = null,

	@field:SerializedName("no_pengiriman")
	val noPengiriman: String? = null,

	@field:SerializedName("no_permintaan")
	val noPermintaan: String? = null,

	@field:SerializedName("kepala_gudang")
	val kepalaGudang: String? = null,

	@field:SerializedName("stor_loc_asal_name")
	val storLocAsalName: String? = null,

	@field:SerializedName("kode_integrasi")
	val kodeIntegrasi: String? = null
)

data class PemakaianDetailItem(

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = "",

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = "",

	@field:SerializedName("unit")
	val unit: String? = "",

	@field:SerializedName("keterangan")
	val keterangan: String? = "",

	@field:SerializedName("nama_material")
	val namaMaterial: String? = "",

	@field:SerializedName("qty_reservasi")
	val qtyReservasi: Int? = 0,

	@field:SerializedName("qty_pemakaian")
	val qtyPemakaian: Int? = 0,

	@field:SerializedName("no_meter")
	val noMeter: String? = "",

	@field:SerializedName("valuation_type")
	val valuationType: String? = "",

	@field:SerializedName("qty_pengeluaran")
	val qtyPengeluaran: Int? = 0
)

data class PemakaianItem(

	@field:SerializedName("no_reservasi")
	val noReservasi: String? = "",

	@field:SerializedName("tanggal_reservasi")
	val tanggalReservasi: String? = "",

	@field:SerializedName("status_pemakaian")
	val statusPemakaian: String? = "",

	@field:SerializedName("sumber")
	val sumber: String? = "",

	@field:SerializedName("no_pemesanan")
	val noPemesanan: String? = "",

	@field:SerializedName("status_kirim_ago")
	val statusKirimAgo: Any? = "",

	@field:SerializedName("daya")
	val daya: String? = "",

	@field:SerializedName("id_pelanggan")
	val idPelanggan: String? = "",

	@field:SerializedName("tanggal_pemakaian")
	val tanggalPemakaian: String? = "",

	@field:SerializedName("jenis_pekerjaan")
	val jenisPekerjaan: String? = "",

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = "",

	@field:SerializedName("no_agenda")
	val noAgenda: String? = "",

	@field:SerializedName("stor_loc")
	val storLoc: String? = "",

	@field:SerializedName("nama_pelanggan")
	val namaPelanggan: String? = "",

	@field:SerializedName("tarif")
	val tarif: String? = "",

	@field:SerializedName("tanggal_bayar")
	val tanggalBayar: Any? = "",

	@field:SerializedName("plant")
	val plant: String? = "",

	@field:SerializedName("status_sap")
	val statusSap: Any? = "",

	@field:SerializedName("tanggal_dokumen")
	val tanggalDokumen: String? = "",

	@field:SerializedName("alamat_pelanggan")
	val alamatPelanggan: String? = "",

	@field:SerializedName("no_pemakaian")
	val noPemakaian: String? = "",

	@field:SerializedName("kode_integrasi")
	val kodeIntegrasi: String? = "",

	@field:SerializedName("tanggal_pengeluaran")
	val tanggalPengeluaran: String? = "",

	@field:SerializedName("no_pk")
	val noPk: String? = "",

	@field:SerializedName("lokasi")
	val lokasi: String? = "",

	@field:SerializedName("kepala_gudang")
	val kepalaGudang: String? = "",

	@field:SerializedName("pejabat_pengesahan")
	val pejabatPengesahan: String? = "",

	@field:SerializedName("pemeriksa")
	val pemeriksa: String? = "",

	@field:SerializedName("penerima")
	val penerima: String? = "",

	@field:SerializedName("nama_kegiatan")
	val namaKegiatan: String? = "",
)

data class SnPemakaianUlpItem(

	@field:SerializedName("no_transaksi")
	val noTransaksi: String? = null,

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("serial_number")
	val serialNumber: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class SnPenerimaanUlpItem(

	@field:SerializedName("no_repackaging")
	val noRepackaging: String? = null,

	@field:SerializedName("nomor_material")
	val nomorMaterial: String? = null,

	@field:SerializedName("serial_number")
	val serialNumber: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: Any? = null
)

data class DataRatingsItem(

	@field:SerializedName("ketepatan")
	val ketepatan: Int? = null,

	@field:SerializedName("rating_quality")
	val ratingQuality: String? = null,

	@field:SerializedName("rating_delivery")
	val ratingDelivery: String? = null,

	@field:SerializedName("selesai_rating")
	val selesaiRating: Boolean? = null,

	@field:SerializedName("rating_doc")
	val ratingDoc: List<String?>? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("rating_response")
	val ratingResponse: String? = null
)

data class PetugasPengujianItem(
	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("jabatan")
	val jabatan: String? = null,

	@field:SerializedName("nama_petugas")
	val namaPetugas: String? = null,

	@field:SerializedName("no_pengujian")
	val noPengujian: String? = null
)

data class MonitoringKomplainItem(

	@field:SerializedName("no_komplain_smar")
	val noKomplainSmar: String? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("finish_date")
	val finishDate: String? = null,

	@field:SerializedName("po_sap_no")
	val poSapNo: String? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("no_komplain")
	val noKomplain: String? = null,

	@field:SerializedName("alasan")
	val alasan: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null,

	@field:SerializedName("tanggal_po")
	val tanggalPo: String? = null
)

data class MonitoringKomplainDetailItem(

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = null,

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = null,

	@field:SerializedName("do_line_item")
	val doLineItem: String? = null,

	@field:SerializedName("tanggal_pengajuan")
	val tanggalPengajuan: String? = null,

	@field:SerializedName("no_packaging")
	val noPackaging: String? = null,

	@field:SerializedName("no_serial")
	val noSerial: String? = null,

	@field:SerializedName("no_komplain")
	val noKomplain: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PegawaiItem(

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("nama_pegawai")
	val namaPegawai: String? = null,

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("plant")
	val plant: String? = null,

	@field:SerializedName("kode_jabatan")
	val kodeJabatan: String? = null,

	@field:SerializedName("nama_jabatan")
	val namaJabatan: String? = null
)

data class DataPenerimaanAkhirItem(

	@field:SerializedName("is_komplained")
	val isKomplained: Boolean? = false,

	@field:SerializedName("no_mat_sap")
	val noMatSap: String? = "",

	@field:SerializedName("stor_loc")
	val storLoc: String? = "",

	@field:SerializedName("no_do_smar")
	val noDoSmar: String? = "null",

	@field:SerializedName("kd_pabrikan")
	val kdPabrikan: String? = "",

	@field:SerializedName("is_rejected")
	val isRejected: Boolean? = false,

	@field:SerializedName("qty_do")
	val qtyDo: Int? = 0,

	@field:SerializedName("is_received")
	val isReceived: Boolean? = false,

	@field:SerializedName("nama_kategori_material")
	val namaKategoriMaterial: String? = "",

	@field:SerializedName("no_packaging")
	val noPackaging: String? = "",

	@field:SerializedName("no_serial")
	val noSerial: String? = ""
)