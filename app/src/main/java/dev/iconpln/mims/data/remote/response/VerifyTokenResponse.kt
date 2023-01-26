package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyTokenResponse(

    @field:SerializedName("data")
    val data: DataVerifyToken? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class DataVerifyToken(

    @field:SerializedName("mail")
    val mail: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("role_id")
    val roleId: Int? = null
)
