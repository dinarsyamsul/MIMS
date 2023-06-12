package dev.iconpln.mims.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.provider.Settings
import androidx.core.content.FileProvider
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.multidex.BuildConfig
import dev.iconpln.mims.data.local.database_local.DatabaseReport
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.CRC32

object DataController {
    private val ROOT_DIRECTORY = Config.DIRECTORY_ROOT_NAME

    enum class FileType {
        MAIN_DIRECTORY, CACHE_DIRECTORY, PHOTO_DEIRECTORY, REPORT_LOG, DATABASE_FILE, PDF_DIRECTORY, DATABASE_REPORT_FILE
    }

    const val RADIUS = 50.0
    const val ACCURACY = 50.0
    private const val LOGIN_PREFERENCES = "Login"

    fun getDirectory(directory: FileType): String {
        return getDirectory(null, directory)
    }

    fun getDirectory(context: Context?, directory: FileType): String {
        if (context != null) {
            return when (directory) {
                DataController.FileType.MAIN_DIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT)
                DataController.FileType.CACHE_DIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/Cache"
                DataController.FileType.PHOTO_DEIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/Image"
                DataController.FileType.REPORT_LOG -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/log_report.pjr"
                DataController.FileType.DATABASE_FILE -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/database.db"
                DataController.FileType.PDF_DIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/pdf"
                DataController.FileType.DATABASE_REPORT_FILE -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/database_report.db"
                else -> ""
            }
        } else {
            return when (directory) {
                DataController.FileType.MAIN_DIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT)
                DataController.FileType.CACHE_DIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/Cache"
                DataController.FileType.PHOTO_DEIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/Image"
                DataController.FileType.REPORT_LOG -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/log_report.pjr"
                DataController.FileType.DATABASE_FILE -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/database.db"
                DataController.FileType.PDF_DIRECTORY -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/pdf"
                DataController.FileType.DATABASE_REPORT_FILE -> StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) + "/database_report.db"
                else -> ""
            }
        }

    }


    fun checkDirectory() {
        checkDirectory(null)
    }

    private fun checkDirectory(context: Context?) {
        val maindir = File(getDirectory(context, DataController.FileType.MAIN_DIRECTORY))
        val cacheDir = File(getDirectory(context, DataController.FileType.CACHE_DIRECTORY))
        val photoDir = File(getDirectory(context, DataController.FileType.PHOTO_DEIRECTORY))
        val pdfDir = File(getDirectory(context, DataController.FileType.PDF_DIRECTORY))

        if (!maindir.exists())
            maindir.mkdir()
        if (!cacheDir.exists())
            cacheDir.mkdir()
        if (!photoDir.exists())
            photoDir.mkdir()
        if (!pdfDir.exists())
            pdfDir.mkdir()
    }

    fun deleteFilesNotInReport(path: String, context: Context) {
        val dir = File(path)
        if (dir.exists() && dir.isDirectory) {
            val listFiles = dir.listFiles()
            val dbReport = DatabaseReport.getDatabase(context)
            for (aFile in listFiles!!) {
                val namaFile = aFile.name
                if (!dbReport.isExistOnNotSentReport(namaFile)) {
                    aFile.delete()
                }
            }
        }
    }

    fun deleteFilesRecursiveNotInReport(path: String, context: Context) {
        val dir = File(path)
        if (dir.exists() && dir.isDirectory) {
            val dbReport = DatabaseReport.getDatabase(context)
            dir.walkTopDown().forEach {
                if(it.isFile) {
                    val filename = it.name
                    if (!dbReport.isExistOnNotSentReport(filename)) {
                        it.delete()
                    }
                }
            }
        }
    }

    fun saveStringToShared(ctx: Context, prefName: String, name: String, data: String) {
        val sp = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val ed = sp.edit().putString(name, data)
        ed.commit()
    }

    fun saveLongToShared(ctx: Context, prefName: String, name: String, data: Long) {
        val sp = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val ed = sp.edit().putLong(name, data)
        ed.commit()
    }

    fun saveBooleanToShared(ctx: Context, prefName: String, name: String, data: Boolean) {
        val sp = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val ed = sp.edit().putBoolean(name, data)
        ed.commit()
    }

    fun getStringFromShared(ctx: Context, prefName: String, name: String, defReturn: String?): String? {
        val sp = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return sp.getString(name, defReturn)
    }

    fun getLongFromShared(ctx: Context, prefName: String, name: String): Long {
        val sp = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return sp.getLong(name, -1)
    }

    fun getBooleanFromShared(ctx: Context, prefName: String, name: String): Boolean {
        val sp = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return sp.getBoolean(name, false)
    }

    fun clearShared(ctx: Context, prefName: String) {
        val sp = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val ed = sp.edit()
        ed.clear()
        ed.commit()
    }

    val waktuForReport: String
        get() {
            val c = Calendar.getInstance()
            val d = c.time
            val tes = (d.time / 1000).toString()
            return tes
        }

    val waktu: String
        get() {
            val c = Calendar.getInstance()
            val d = c.time
            val tes = d.time.toString()
            return tes
        }

    val waktuInLong: Long
        get() {
            val c = Calendar.getInstance()
            val d = c.time

            return d.time
        }

    fun generateArray(size: Int): Array<Int> {
        val arr = Array<Int>(size,{i->0})
        for (i in 0..size - 1) {
            arr[i] = i + 1
        }

        return arr
    }

    fun <T> getStringArray(list: List<T>): Array<String> {
        val arr = Array<String>(list.size,{i->""})
        for (i in list.indices) {
            arr[i] = (i + 1).toString() + ". " + list[i].toString()
        }

        return arr
    }

    fun hashCRC32(text: String): String {
        return hashCRC32(text.toByteArray())
    }

    fun hashCRC32(bytes: ByteArray): String {
        val crc: String
        val crc32 = CRC32()
        crc32.update(bytes)
        val value = crc32.value
        crc = java.lang.Long.toHexString(value)

        return crc
    }

    fun setScreen(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val size = activity.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        if (size == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else if (size == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    fun transformTime(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz
        val localTime = sdf.format(cal.time)

        return localTime
    }

    fun transformTime(time: Long, format: String): String {
        val sdf = SimpleDateFormat(format)
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz
        val localTime = sdf.format(cal.time)
        return localTime
    }

    fun transformDate(time: Long): String {
        val sdf = SimpleDateFormat("yyMMdd")
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz

        val localTime = sdf.format(cal.time)

        return localTime
    }

    fun transformFullTime(time: Long): String {
        val sdf = SimpleDateFormat("d, MMM yyyy HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz

        val localTime = sdf.format(cal.time)

        return localTime
    }

    fun readpdf(c: Activity, title: String) {
        val pdfFile = File(getDirectory(DataController.FileType.PDF_DIRECTORY) + "/" + title + ".pdf")
        if (pdfFile.exists()) {
            val path = Uri.fromFile(pdfFile)
            val pdfIntent = Intent(Intent.ACTION_VIEW)
            pdfIntent.setDataAndType(path, "application/pdf")
            pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            try {
                c.startActivity(pdfIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(c, "No Application available to view pdf", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getPxFromSP(ctx: Context, sp: Int): Float {
        val metrics = ctx.resources.displayMetrics
        return sp * metrics.scaledDensity
    }

    fun getVersion(context: Context): String {
        var v = ""
        try {
            v = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return v
    }

     fun findBinary(binaryName: String): Boolean {
        var found = false
        if (!found) {
            val places = arrayOf("/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/")
            for (where in places) {
                if (File(where + binaryName).exists()) {
                    found = true

                    break
                }
            }
        }
        return found
    }

    val isRooted: Boolean
        get() = findBinary("su")

    fun getAndroidID(context: Context): String {
        val android_id = Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID)

        return android_id
    }

    fun getDeviceData(context: Context): String {
        val osVersion = System.getProperty("os.version") // OS version
        val sdk = android.os.Build.VERSION.SDK     // API Level
        val device = android.os.Build.DEVICE         // Device
        val model = android.os.Build.MODEL          // Model
        val product = android.os.Build.PRODUCT
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = ""//telephonyManager.deviceId
        return "$osVersion,$sdk,$device,$model,$product,$imei"
    }

    fun getEmptyView(activity: Activity): View {
        val tv = TextView(activity)
        tv.text = "Tidak Ada Data"
        return tv
    }

    fun setGroupEnabled(grup: RadioGroup, enable: Boolean) {
        for (i in 0..grup.childCount - 1) {
            grup.getChildAt(i).isEnabled = enable
        }
    }

    fun getString(number: Int): String {
        return when (number) {
            0 -> "00"
            in 1..9 -> "0" + number
            else -> number.toString()
        }
    }

    fun getPopup(a: Activity, title: String, text: String): AlertDialog.Builder {
        val alert = AlertDialog.Builder(a)
        alert.setTitle(title)
        alert.setMessage(text)

        alert.setNegativeButton("Batal") { dialog, which ->
            dialog.dismiss()
        }
        return alert
    }

    fun finishButton(button: Button, activity: Activity) {
        button.paintFlags = button.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        button.setTextColor(activity.resources.getColor(android.R.color.holo_green_dark))
    }

    fun sendEmail(activity: Activity, mailto: Array<String>, subject: String, message: String, attachment: File?) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "vnd.android.cursor.dir/email"
        val to = mailto
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        if (attachment != null) {
            val filelocation = attachment
            val path = Uri.fromFile(filelocation)
            emailIntent.putExtra(Intent.EXTRA_STREAM, path)
        }
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

        activity.startActivity(Intent.createChooser(emailIntent, message))
    }

    fun getDataLoginFromShared(context: Context): DataHelper.DataLogin? {
        var dataLogin: DataHelper.DataLogin? = null
        if (getBooleanFromShared(context, LOGIN_PREFERENCES, "login")) {
            val sp = context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE)

            val id_karyawan = sp.getString("id_karyawan", "")
            val username = sp.getString("username", "")
            val nama_karyawan = sp.getString("nama_karyawan", "")
            val nama_role = sp.getString("nama_role", "")
            val nama_tl = sp.getString("nama_tl","")
            val hari_ke = sp.getString("hari_ke","")
            val tanggal = sp.getString("tanggal_login","")
            val role_id = sp.getString("role_id","")

            dataLogin = DataHelper.DataLogin(
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

        return dataLogin
    }

    fun updateAPK(activity: Activity, path: String) {
        Log.d("onUpdateApk", "yes")
        val toInstall =  File(path)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Log.d("onVersionCodeUpperN", "yes")
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
                val apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", toInstall)
                val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
                intent.data = apkUri
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                activity.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Log.d("onVersionCodeBelowN", "yes")
            val apkUri = Uri.fromFile(toInstall)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }
    }

    fun calculateDistance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Int {
        val lat1 = latitude1
        val lng1 = longitude1
        val lat2 = latitude2
        val lng2 = longitude2

        val earthRadius = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val dist = earthRadius * c

        //	    Log.d("Get distance form "+lng1+","+lat1, "to "+lng2+","+lat2);

        return Math.round(dist).toInt()
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap?, pixels: Int): Bitmap? {
        var output: Bitmap? = null
        if (bitmap != null)
            output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        else
            return null

        if (output != null) {
            val canvas = Canvas(output)

            val color = 0xff424242.toInt()
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = pixels.toFloat()

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
        } else {
            return bitmap
        }

        return output
    }

    fun scaleDown(realImage: Bitmap, maxImageSize: Float,
                  filter: Boolean): Bitmap {
        val ratio = Math.min(
                maxImageSize.toFloat() / realImage.width,
                maxImageSize.toFloat() / realImage.height)
        val width = Math.round(ratio.toFloat() * realImage.width)
        val height = Math.round(ratio.toFloat() * realImage.height)

        val newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter)
        return newBitmap
    }
}
