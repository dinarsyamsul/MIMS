package dev.iconpln.mims.utils

import android.os.Environment
import java.io.File

object StorageUtils {
    val DIRECTORY_ROOT = 1
    val DIRECTORY_CACHE = 2
    val DIRECTORY_IMAGE = 3
    val DIRECTORY_REPORT_LOG = 4
    val DIRECTORY_DATABASE = 5
    val DIRECTORY_PDF = 6

    fun getDirectory(directory: Int): String {
        when (directory) {
            DIRECTORY_ROOT -> return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Config.DIRECTORY_ROOT_NAME
            DIRECTORY_CACHE -> return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Config.DIRECTORY_ROOT_NAME + "/Caches"
            DIRECTORY_IMAGE -> return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Config.DIRECTORY_ROOT_NAME + "/Images"
            DIRECTORY_REPORT_LOG -> return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Config.DIRECTORY_ROOT_NAME + "/log_report.sys"
            DIRECTORY_DATABASE -> return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Config.DIRECTORY_ROOT_NAME + "/Databases"
            DIRECTORY_PDF -> return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + Config.DIRECTORY_ROOT_NAME + "/Pdf"


//            DIRECTORY_ROOT -> return Environment.getExternalStoragePublicDirectory("").toString() + "/Android/media/" + Config.DIRECTORY_ROOT_NAME
//            DIRECTORY_CACHE -> return Environment.getExternalStoragePublicDirectory("").toString() + "/Android/media/" + Config.DIRECTORY_ROOT_NAME + "/Caches"
//            DIRECTORY_IMAGE -> return Environment.getExternalStoragePublicDirectory("").toString() + "/Android/media/" + Config.DIRECTORY_ROOT_NAME + "/Images"
//            DIRECTORY_REPORT_LOG -> return Environment.getExternalStoragePublicDirectory("").toString() + "/Android/media/" + Config.DIRECTORY_ROOT_NAME + "/log_report.sys"
//            DIRECTORY_DATABASE -> return Environment.getExternalStoragePublicDirectory("").toString() + "/Android/media/" + Config.DIRECTORY_ROOT_NAME + "/Databases"
            DIRECTORY_PDF -> return Environment.getExternalStoragePublicDirectory("").toString() + "/Android/media/" + Config.DIRECTORY_ROOT_NAME + "/Pdf"

            else -> return ""
        }
    }

    fun createDirectories() {
        val mainDirectory = File(getDirectory(DIRECTORY_ROOT))
        val cacheDirectory = File(getDirectory(DIRECTORY_CACHE))
        val imageDirectory = File(getDirectory(DIRECTORY_IMAGE))
        val databaseDirectory = File(getDirectory(DIRECTORY_DATABASE))
        val pdfDirectory = File(getDirectory(DIRECTORY_PDF))


        if (!mainDirectory.exists()) {
            mainDirectory.mkdir()
        }
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdir()
        }
        if (!imageDirectory.exists()) {
            imageDirectory.mkdir()
        }
        if (!databaseDirectory.exists()) {
            databaseDirectory.mkdir()
        }
        if (!pdfDirectory.exists()) {
            pdfDirectory.mkdir()
        }
    }
}
