package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class MaterialResponse(

    @field:SerializedName("data")
    val data: List<DataItemMaterial>,

    @field:SerializedName("status")
    val status: String
)

data class DataItemMaterial(

    @field:SerializedName("nama_pabrikan")
    val namaPabrikan: String,

    @field:SerializedName("no_purchase_order")
    val noPurchaseOrder: String,

    @field:SerializedName("nomor_material")
    val nomorMaterial: String,

    @field:SerializedName("spesifikasi_material")
    val spesifikasiMaterial: String,

    @field:SerializedName("material_group")
    val materialGroup: String,

    @field:SerializedName("tgl_produksi")
    val tglProduksi: String,

    @field:SerializedName("flag_delete")
    val flagDelete: String,

    @field:SerializedName("serial_number")
    val serialNumber: String,

    @field:SerializedName("source")
    val source: String,

    @field:SerializedName("sources")
    val sources: String,

    @field:SerializedName("no_produksi")
    val noProduksi: String,

    @field:SerializedName("tahun_produksi")
    val tahunProduksi: String,

    @field:SerializedName("no_sert_metrologi")
    val noSertMetrologi: String,

    @field:SerializedName("kode_pabrikan")
    val kodePabrikan: String,

    @field:SerializedName("updated_date")
    val updatedDate: String,

    @field:SerializedName("masa_garansi")
    val masaGaransi: String,

    @field:SerializedName("id_material")
    val idMaterial: String,

    @field:SerializedName("kategori_material")
    val kategoriMaterial: String
)