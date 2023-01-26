package dev.iconpln.mims.data.local.databasereport

import dev.iconpln.mims.utils.StorageUtils
import org.apache.http.entity.mime.FormBodyPart
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException

class ReportParameter(var idParameter: String, var idReport: String, var namaParameter: String, var valueParameter: String, var tipeParameter: Int) {

    @Throws(UnsupportedEncodingException::class)
    fun toFormBodyPart(): FormBodyPart {
        return if (tipeParameter == FILE) {
            if(File(valueParameter).exists()) {
                FormBodyPart(namaParameter, FileBody(File(valueParameter)))
            } else {
                val pathName = StorageUtils.getDirectory(StorageUtils.DIRECTORY_IMAGE) + "/blank_image"
                val blankFile = File(pathName)
                if (!blankFile.exists()) {
                    try {
                        blankFile.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                FormBodyPart(namaParameter, FileBody(File(pathName)))
            }
        } else {
            FormBodyPart(namaParameter, StringBody(valueParameter))
        }
    }

    companion object {
        const val TEXT = 0
        const val FILE = 1
    }

}
