package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class HitEmailResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)
