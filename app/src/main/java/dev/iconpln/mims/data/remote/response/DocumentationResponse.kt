package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class DocumentationResponse(

	@field:SerializedName("doc")
	val doc: Doc? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Doc(

	@field:SerializedName("array")
	val array: List<String>
)
