package dev.iconpln.mims.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Patterns
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import org.joda.time.DateTime
import java.io.File
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Helper {
    val isRooted: Boolean
        get() = findBinary("su")

    val deviceData: String
        get() {
            val osVersion = System.getProperty("os.version")
            val sdk = Build.VERSION.SDK_INT
            val device = Build.DEVICE
            val model = Build.MODEL
            val product = Build.PRODUCT
            return "$osVersion,$sdk,$device,$model,$product"
        }

    fun setSpinnerValue(context: Context, arrayId: Int, spinner: Spinner, value: String) {
        val arr = context.resources.getStringArray(arrayId)
        val index = Arrays.asList(*arr).indexOf(value)
        spinner.setSelection(index)
    }

    fun convertZeroToEmpty(intValue: Int, isCompleted: Boolean): String {
        var value = NumberFormat.getNumberInstance(Locale.US).format(intValue.toLong())
        if (!isCompleted) {
            value = if (intValue == 0) "" else value
        }
        return value
    }

    fun convertZeroToEmpty(intValue: Int): String {
        return if (intValue == 0) "" else intValue.toString()
    }

    fun convertYesNoToNumber(value: String): String {
        return when {
            value.toLowerCase() == "yes" -> "1"
            value.toLowerCase() == "no" -> "0"
            else -> value
        }
    }

    fun getVersionApp(context: Context): String {
        return "v${getVersion(context)}"
    }

    fun generateVisitId(surveyorId: Int, storeId: Int): String {
        return "V.$surveyorId.$storeId.${DateTime.now().toString("yyMMddHHmmssSS")}"
    }

    fun generateStoreId(surveyorId: Int, areaId: Int): String {
        return "S.$surveyorId.$areaId.${DateTime.now().toString("yyMMddHHmmssSS")}"
    }
    
    fun generateStoreCode(surveyorId: Int, areaId: Int): String {
        return "SC.$surveyorId.$areaId.${DateTime.now().toString("yyMMddHHmmssSS")}"
    }

    fun generateReportId(prefix: String, surveyorId: Int, storeId: Int, referenceId: Int): String {
        return "$prefix.$surveyorId.$storeId.$referenceId.${DateTime.now().toString("yyMMddHHmmssSS")}"
    }

    fun getDistance(location: Location, longitude: Double, latitude: Double): Double {
        return getDistance(location.longitude, location.latitude, longitude, latitude)
    }

    fun getDistance(longitude1: Double, latitude1: Double, longitude2: Double, latitude2: Double): Double {
        val earthRadius = 6371000.0
        val dLat = Math.toRadians(latitude2 - latitude1)
        val dLng = Math.toRadians(longitude2 - longitude1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
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

    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun findBinary(binaryName: String): Boolean {
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

    fun checkPermissionExternalStorage(activity: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    fun removeComma(string: String): String {
        return if (string.contains(",")) {
            string.replace(",", "")
        } else {
            string
        }
    }

    fun convertMeterToKm(distance: Float): String {
        val df1 = DecimalFormat("#")
        val df2 = DecimalFormat("#.##")
        return if (distance > 1000) {
            df2.format((distance / 1000).toDouble()) + " km"
        } else {
            df1.format(distance.toDouble()) + " meter"
        }
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return ""
    }

    fun getOsArchitecture(): String {
        return try {
            System.getProperty("os.arch").toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun getAbi(): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Build.SUPPORTED_ABIS[0].toString()
            } else {
                Build.CPU_ABI
            }
        } catch (e: Exception) {
            ""
        }
    }

    //CHECK EMAIL
    fun checkEmailValidity(emailFormat: AppCompatEditText): Boolean {
        val getEmail = emailFormat.text.toString()
        val getEnd: Boolean

        //CHECK STARTING STRING IF THE USER
        //entered @gmail.com / @yahoo.com / @outlook.com only
        val getResult = !TextUtils.isEmpty(getEmail) && Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()

        //CHECK THE EMAIL EXTENSION IF IT ENDS CORRECTLY
        getEnd = getEmail.contains("@") && getEmail.contains(".")

        //TEST THE START AND END
        return getResult && getEnd
    }

    fun getFloatWith2Digit(floatNum: Float): String? {
        val format = DecimalFormat("0.##")
        return format.format(floatNum)
    }
}
