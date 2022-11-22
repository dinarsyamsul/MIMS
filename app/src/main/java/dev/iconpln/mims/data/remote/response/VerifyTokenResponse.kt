package dev.iconpln.mims.data.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyTokenResponse(

	@field:SerializedName("data")
	val data: List<DataItemVerify>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataItemVerify(

	@field:SerializedName("modifyby")
	val modifyby: String,

	@field:SerializedName("user_password")
	val userPassword: Any,

	@field:SerializedName("mail")
	val mail: String,

	@field:SerializedName("regional")
	val regional: String,

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("unitupi")
	val unitupi: String,

	@field:SerializedName("createdate")
	val createdate: String,

	@field:SerializedName("last_name")
	val lastName: String,

	@field:SerializedName("flag_ldap")
	val flagLdap: String,

	@field:SerializedName("email_token")
	val emailToken: String,

	@field:SerializedName("kd_jabatan")
	val kdJabatan: String,

	@field:SerializedName("user_active")
	val userActive: String,

	@field:SerializedName("createby")
	val createby: String,

	@field:SerializedName("modifydate")
	val modifydate: String,

	@field:SerializedName("foto")
	val foto: Any,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("role_id")
	val roleId: String,

	@field:SerializedName("unitap")
	val unitap: String,

	@field:SerializedName("device_token")
	val deviceToken: String,

	@field:SerializedName("unitup")
	val unitup: String,

	@field:SerializedName("phone_number")
	val phoneNumber: String,

	@field:SerializedName("user_token")
	val userToken: String,

	@field:SerializedName("first_name")
	val firstName: String
)
