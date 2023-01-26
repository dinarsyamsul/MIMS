package dev.iconpln.mims.utils

import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class DataHelper {

    data class DataLogin(
        val id_karyawan: String?,
        val username: String?,
        val nama_karyawan: String?,
        val nama_role: String?,
        val nama_tl: String?,
        val hari_ke: String?,
        val tanggal_login: String?,
        val role_id: String? = ""
    ) {
        companion object {

            @Throws(JSONException::class)
            fun fromJSON(jsonObject: JSONObject): DataLogin {
                val id_karyawan = jsonObject.getString("id_karyawan")
                val username = jsonObject.getString("username")
                val nama_karyawan = jsonObject.getString("nama_karyawan")
                val nama_role = jsonObject.getString("nama_role")
                val nama_tl = jsonObject.getString("nama_tl")
                val hari_ke = jsonObject.getString("hari_ke")
                val tanggal = jsonObject.getString("tanggal_login")
                val role_id = jsonObject.getString("role_id")

                return DataLogin(
                    id_karyawan,
                    username,
                    nama_karyawan,
                    nama_role,
                    nama_tl,
                    hari_ke,
                    tanggal,
                    role_id
                )
            }
        }
    }

    class AppZip(source: String, output: String) {
        internal var fileList: MutableList<String>
        private var OUTPUT_ZIP_FILE = "C:\\MyFile.zip"
        private var SOURCE_FOLDER = "C:\\testzip"

        init {
            fileList = ArrayList<String>()
            OUTPUT_ZIP_FILE = output
            SOURCE_FOLDER = source
        }


        fun run() {
            generateFileList(File(SOURCE_FOLDER))
            zipIt(OUTPUT_ZIP_FILE)
        }

        /**
         * Zip it
         * @param zipFile output ZIP file location
         */
        fun zipIt(zipFile: String) {

            val buffer = ByteArray(1024)

            try {

                val fos = FileOutputStream(zipFile)
                val zos = ZipOutputStream(fos)

                println("Output to Zip : " + zipFile)

                for (file in this.fileList) {

                    println("File Added : " + file)
                    val ze = ZipEntry(file)
                    zos.putNextEntry(ze)

                    val `in` = FileInputStream(SOURCE_FOLDER + File.separator + file)

                    var len: Int = `in`.read(buffer)
                    while (len > 0) {
                        zos.write(buffer, 0, len)
                        len = `in`.read(buffer)
                    }

                    `in`.close()
                }

                zos.closeEntry()
                //remember close it
                zos.close()

                println("Done")
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        /**
         * Traverse a directory and get all files,
         * and add the file into fileList
         * @param node file or directory
         */
        fun generateFileList(node: File) {

            //add file only
            if (node.isFile && !node.absolutePath.endsWith(".apk")) {
                fileList.add(generateZipEntry(node.absoluteFile.toString()))
            }

            if (node.isDirectory) {
                val subNote = node.list()
                for (filename in subNote) {
                    generateFileList(File(node, filename))
                }
            }

        }

        /**
         * Format the file path for zip
         * @param file file path
         * *
         * @return Formatted file path
         */
        private fun generateZipEntry(file: String): String {
            return file.substring(SOURCE_FOLDER.length + 1, file.length)
        }
    }
}
