package dev.iconpln.mims.utils

import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    const val SECOND = 0
    const val MINUTE = 1
    const val HOUR = 2
    const val DAY = 3

    val currentUtcString: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            return sdf.format(Date())
        }


    fun getCurrentTimeStamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
    }

    val currentUtc: Long
        get() = System.currentTimeMillis() / 1000L

    val waktuForReport: String
        get() {
            val c = Calendar.getInstance()
            val d = c.time
            return (d.time / 1000).toString()
        }


    val waktu: String
        get() {
            val c = Calendar.getInstance()
            val d = c.time
            return d.time.toString()
        }

    val waktuDescription: String
        get() {
            val dateTime = LocalDateTime.now()
            return dateTime.toString()
        }

    val waktuInLong: Long
        get() {
            val c = Calendar.getInstance()
            val d = c.time

            return d.time
        }

    fun formatDateString(datetime: String, fromPattern: String, toPattern: String): String {
        return try {
            val dt = convertStringToDate(datetime, fromPattern)
            convertDateToString(dt, toPattern)
        } catch (e: Exception) {
            ""
        }
    }

    fun subtractingDate(startDate: Date, endDate: Date): LongArray {
        //milliseconds
        var different = endDate.time - startDate.time

        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli

        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli

        val elapsedSeconds = different / secondsInMilli

        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds)
        return longArrayOf(elapsedDays, elapsedHours, elapsedMinutes)

        //return Integer.parseInt(String.valueOf(elapsedDays)+String.valueOf(elapsedHours)+String.valueOf(elapsedMinutes));
    }

    fun convertDateToString(date: Date, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern)
        return try {
            dateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun convertStringToDate(datetime: String, pattern: String): Date {
        val format = SimpleDateFormat(pattern)
        return try {
            format.parse(datetime)
        } catch (e: Exception) {
            e.printStackTrace()
            DateTime.parse("1970-01-01 00:00:00").toDate()
        }
    }

    fun convertStringToCalendar(strDate: String, pattern: String): Calendar {
        val cal = Calendar.getInstance()
        try {
            val sdf = SimpleDateFormat(pattern)
            val date = sdf.parse(strDate)
            cal.time = date
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cal
    }

    fun transformTime(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz

        return sdf.format(cal.time)
    }

    fun transformTime(time: Long, format: String): String {
        val sdf = SimpleDateFormat(format)
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz

        return sdf.format(cal.time)
    }

    fun transformDate(time: Long): String {
        val sdf = SimpleDateFormat("yyMMdd")
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz

        return sdf.format(cal.time)
    }

    fun transformFullTime(time: Long): String {
        val sdf = SimpleDateFormat("d, MMM yyyy HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        val tz = cal.timeZone
        sdf.timeZone = tz

        return sdf.format(cal.time)
    }


    fun compareDate(d1: String, d2: String, compareType: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date1 = sdf.parse(d1)
        val date2 = sdf.parse(d2)

        if(compareType == "after") {
            return date1.after(date2)
        }

        if(compareType == "before") {
            return date1.before(date2)
        }

        if(compareType == "equals") {
            return date1.equals(date2)
        }

        return false
    }

    fun getDiffDateTime(startDateTime: DateTime, endDateTime: DateTime, returnType: Int): Long {
        val dateStart = startDateTime.toString("MM/dd/yyyy HH:mm:ss")
        val dateStop = endDateTime.toString("MM/dd/yyyy HH:mm:ss")
        val format = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")

        val d1: Date?
        val d2: Date?

        try {
            d1 = format.parse(dateStart)
            d2 = format.parse(dateStop)
            val diff = d2.time - d1.time
            when (returnType) {
                SECOND -> {
                    return diff / 1000 % 60
                }
                MINUTE -> {
                    return diff / (60 * 1000) % 60
                }
                HOUR -> {
                    return diff / (60 * 60 * 1000) % 24
                }
                DAY -> {
                    return diff / (24 * 60 * 60 * 1000)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    fun convertStringToMiliseconds(datetime: String, pattern: String): Long {
        val sdf = SimpleDateFormat(pattern)
        try {
            val mDate = sdf.parse(datetime)
            return mDate.time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun convertDate(date: String): String {
        val simpleDateFormatDefault = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatNew = SimpleDateFormat("dd-MM-yyyy")
        val convertedCurrentDate = simpleDateFormatNew.format(simpleDateFormatDefault.parse(date))
        return convertedCurrentDate
    }
}