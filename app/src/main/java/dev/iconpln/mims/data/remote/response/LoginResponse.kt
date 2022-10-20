package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("data")
    val data: List<AnotherDataItem>
)

data class AnotherDataItem(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("unitUPI")
    val unitUPI: String,

    @field:SerializedName("namaUnitUP")
    val namaUnitUP: String,

    @field:SerializedName("namaRegional")
    val namaRegional: String,

    @field:SerializedName("namaStorLoc")
    val namaStorLoc: String,

    @field:SerializedName("namaPlant")
    val namaPlant: String,

    @field:SerializedName("regional")
    val regional: String,

    @field:SerializedName("roleId")
    val roleId: String,

    @field:SerializedName("photo")
    val photo: String,

    @field:SerializedName("namaUnitUPI")
    val namaUnitUPI: String,

    @field:SerializedName("storLoc")
    val storLoc: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("unitAP")
    val unitAP: String,

    @field:SerializedName("isCode")
    val isCode: String,

    @field:SerializedName("authdata")
    val authdata: String,

    @field:SerializedName("plant")
    val plant: String,

    @field:SerializedName("roleName")
    val roleName: String,

    @field:SerializedName("unitUP")
    val unitUP: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("gudangInduk")
    val gudangInduk: String,

    @field:SerializedName("namaUnitAP")
    val namaUnitAP: String,

    @field:SerializedName("username")
    val username: String
)
