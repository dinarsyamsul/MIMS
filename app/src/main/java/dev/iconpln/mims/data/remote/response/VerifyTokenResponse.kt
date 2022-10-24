package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyTokenResponse(

	@field:SerializedName("data")
	val data: List<DataItemVerify>,

	@field:SerializedName("status")
	val status: String
)

data class DataItemVerify(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("modifyby")
	val modifyby: Any,

	@field:SerializedName("user_password")
	val userPassword: String,

	@field:SerializedName("mail")
	val mail: String,

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("unitupi")
	val unitupi: String,

	@field:SerializedName("createdate")
	val createdate: String,

	@field:SerializedName("flag_ldap")
	val flagLdap: String,

	@field:SerializedName("email_token")
	val emailToken: Any,

	@field:SerializedName("user_active")
	val userActive: String,

	@field:SerializedName("createby")
	val createby: Any,

	@field:SerializedName("modifydate")
	val modifydate: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("role_id")
	val roleId: String,

	@field:SerializedName("user_token")
	val userToken: String
)
