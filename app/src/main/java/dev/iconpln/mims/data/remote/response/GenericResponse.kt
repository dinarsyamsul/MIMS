package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class GenericResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
