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

    @field:SerializedName("no_purchase_order")
    val noPurchaseOrder: String,

    @field:SerializedName("qty_po")
    val qtyPo: String,

    @field:SerializedName("update_date")
    val updateDate: String,

    @field:SerializedName("create_by")
    val createBy: String,

    @field:SerializedName("unit")
    val unit: String,

    @field:SerializedName("stor_loc")
    val storLoc: String,

    @field:SerializedName("delivery_date")
    val deliveryDate: String,

    @field:SerializedName("no_material_sap")
    val noMaterialSap: String,

    @field:SerializedName("vendor")
    val vendor: String,

    @field:SerializedName("nama_material")
    val namaMaterial: String,

    @field:SerializedName("satuan")
    val satuan: String,

    @field:SerializedName("plant")
    val plant: String,

    @field:SerializedName("lead_time")
    val leadTime: String,

    @field:SerializedName("deskripsi")
    val deskripsi: String,

    @field:SerializedName("kode_pabrikan")
    val kodePabrikan: String,

    @field:SerializedName("create_date")
    val createDate: String,

    @field:SerializedName("update_by")
    val updateBy: String
)