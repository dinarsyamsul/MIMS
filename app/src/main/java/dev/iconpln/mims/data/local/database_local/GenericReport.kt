package dev.iconpln.mims.data.local.database_local

import android.util.Log
import dev.iconpln.mims.utils.Config
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException

class GenericReport(internal var idReport: String, internal var user_id: String, nama: String, internal var deskripsiReport: String, internal var urlReport: String, internal var tanggalReport: String, internal var status_done: Int, internal var waktuReport: Long, listParameter: List<ReportParameter>) :
    AbstractReport {
    var namaReport: String
        internal set
    var parameterList: List<ReportParameter>
        internal set

    private var returnString: String? = null

    init {
        namaReport = nama
        parameterList = listParameter
    }

    private val multipartEntity: MultipartEntity
        @Throws(UnsupportedEncodingException::class)
        get() = toMultipartEntity(parameterList)

    override fun sendReport(): Boolean {
        try {
            val client = DefaultHttpClient()
            val login = HttpPost(urlReport)

            Log.i("GenericReport.kt", "ReportName: " + namaReport)
            Log.i("GenericReport.kt", "Url: " + urlReport)

            val me = multipartEntity
            login.entity = me
            login.addHeader("jwt", user_id)

            val response = client.execute(login)
            val responseEntity = response.entity
            returnString = EntityUtils.toString(responseEntity)

            Log.i("GenericReport.kt", "Return: " + returnString)

            val jsonreturn = JSONObject(returnString)
            val status = jsonreturn.getString("status")

            return status == "success"
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return false
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        } catch (e: ClientProtocolException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    override fun getReturn(): String? {
        return if (returnString != null)
            returnString
        else
            null
    }

    override fun getDescription(): String {
        return deskripsiReport
    }

    companion object {
        val STATUS_DONE = 1
        val STATUS_NOT_DONE = 0

        @Throws(UnsupportedEncodingException::class)
        fun toMultipartEntity(parameters: List<ReportParameter>?): MultipartEntity {
            val builder = MultipartEntity()

            if (parameters != null)
                for (parameter in parameters) {
                    Log.i("GenericReport.kt", parameter.namaParameter + ": " + parameter.valueParameter + ".")
                    builder.addPart(parameter.toFormBodyPart())
                }

            return builder
        }
    }
}
