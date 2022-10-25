package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataItem(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("modifyby")
	val modifyby: Any,

	@field:SerializedName("user_password")
	val userPassword: Any,

	@field:SerializedName("mail")
	val mail: String,

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("unitupi")
	val unitupi: Any,

	@field:SerializedName("createdate")
	val createdate: Any,

	@field:SerializedName("flag_ldap")
	val flagLdap: Any,

	@field:SerializedName("email_token")
	val emailToken: Any,

	@field:SerializedName("user_active")
	val userActive: Any,

	@field:SerializedName("createby")
	val createby: Any,

	@field:SerializedName("modifydate")
	val modifydate: Any,

	@field:SerializedName("user_id")
	val userId: Any,

	@field:SerializedName("role_id")
	val roleId: Any,

	@field:SerializedName("device_token")
	val deviceToken: Any
)
