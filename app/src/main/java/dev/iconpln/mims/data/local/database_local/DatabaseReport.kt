package dev.iconpln.mims.data.local.database_local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.util.Log
import dev.iconpln.mims.utils.DataController
import org.joda.time.LocalDate
import java.util.ArrayList

class DatabaseReport constructor(context: Context) : SQLiteOpenHelper(context, DataController.getDirectory(
    DataController.FileType.DATABASE_REPORT_FILE), null, 1) {

    private var sqlInsert: SQLiteStatement? = null

    init {
        setWriteAheadLoggingEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + "report" + " (id_report TEXT PRIMARY KEY,user_id TEXT, nama_report TEXT, deskripsi_report TEXT, url_report TEXT, tanggal_report TEXT,status_done INT,waktu_report INT)")
        db.execSQL("CREATE TABLE " + "parameter" + "(id_parameter TEXT, report_id TEXT,nama_parameter TEXT, nilai_parameter TEXT, tipe_parameter INT, PRIMARY KEY(id_parameter, report_id), FOREIGN KEY(report_id) REFERENCES report(id_report))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    val reportToBeSent: List<GenericReport>
        get() {
            val db = readableDatabase

            Log.i("DatabaseReport.kt", "Report will be Sent: test")
            val listreport = getReport(null, 0, null, db)
            db.close()

            return listreport
        }

    val reportSent: List<GenericReport>
        get() {
            val db = readableDatabase
            Log.i("DatabaseReport.kt", "Report Sent: get report sent")
            val listreport = getReport(LocalDate.now().toString("yyyy-MM-dd"), 1, null, db)
            db.close()

            return listreport
        }

    private fun getReport(date: String?, done: Int, user_id: String?, db: SQLiteDatabase?): List<GenericReport> {
        var database = db

        if (database == null || !database.isOpen)
            database = readableDatabase

        val selection: String?
        if (date != null && done != -1 && user_id != null) {
            selection = "tanggal_report = '$date' and status_done = '$done' and user_id = '$user_id'"
        } else if (date != null && done != -1 && user_id == null) {
            selection = "tanggal_report = '$date' and status_done = '$done'"
        } else if (date != null && done == -1 && user_id != null) {
            selection = "tanggal_report = '$date' and user_id = '$user_id'"
        } else if (date != null && done == -1 && user_id == null) {
            selection = "tanggal_report = '$date'"
        } else if (date == null && done != -1 && user_id != null) {
            selection = " status_done = '$done' and user_id = '$user_id'"
        } else if (date == null && done != -1 && user_id == null) {
            selection = " status_done = '$done'"
        } else if (date == null && done == -1 && user_id != null) {
            selection = "user_id = '$user_id'"
        } else if (date == null && done == -1 && user_id == null) {
            selection = null
        } else {
            selection = null
        }

        Log.i("DatabaseReport.kt", "Selection: " + selection)

        val listReport = ArrayList<GenericReport>()
        val cursor = database!!.query("report", arrayOf("id_report", "user_id", "nama_report", "deskripsi_report", "url_report", "tanggal_report", "status_done", "waktu_report", "jwt"),
            selection, null, null, null, null)
        if (cursor.moveToFirst()) {
            var report_id: String
            var userid: String
            var nama_report: String
            var deskripsi_report: String
            var url_report: String
            var tanggal_report: String
            var status_done: Int
            var waktu_report: Long
            var jwt: String

            var report: GenericReport

            while (!cursor.isAfterLast) {
                report_id = cursor.getString(0)
                userid = cursor.getString(1)
                nama_report = cursor.getString(2)
                deskripsi_report = cursor.getString(3)
                url_report = cursor.getString(4)
                tanggal_report = cursor.getString(5)
                status_done = cursor.getInt(6)
                waktu_report = cursor.getLong(7)
                jwt = cursor.getString(8)

                report = GenericReport(report_id, userid, nama_report, deskripsi_report, url_report, tanggal_report, status_done, waktu_report, getParameterByIdReport(report_id, db),jwt)

                listReport.add(report)
                if (!cursor.moveToNext())
                    break
            }
        }
        cursor.close()

        return listReport
    }

    private fun getParameterByIdReport(idReport: String, db: SQLiteDatabase?): List<ReportParameter> {
        var database = db
        if (database == null || !database.isOpen)
            database = readableDatabase

        val listParameter = ArrayList<ReportParameter>()
        val cursor = database!!.query("parameter", arrayOf("id_parameter", "report_id", "nama_parameter", "nilai_parameter", "tipe_parameter"),
            "report_id = ? ", arrayOf(idReport), null, null, null)
        if (cursor.moveToFirst()) {
            var id_parameter: String
            var id_report: String
            var nama_parameter: String
            var value_parameter: String
            var tipe_parameter: Int
            var parameter: ReportParameter

            while (!cursor.isAfterLast) {
                id_parameter = cursor.getString(0)
                id_report = cursor.getString(1)
                nama_parameter = cursor.getString(2)
                value_parameter = cursor.getString(3)
                tipe_parameter = cursor.getInt(4)


                parameter = ReportParameter(id_parameter, id_report, nama_parameter, value_parameter, tipe_parameter)

                listParameter.add(parameter)
                if (!cursor.moveToNext())
                    break
            }
        }
        cursor.close()
        return listParameter
    }

    fun isExistOnNotSentReport(namaFile: String): Boolean {
        val db = readableDatabase
        val sql = "select id_report, nama_parameter, nilai_parameter from report join parameter on id_report = report_id and status_done = 0 and tipe_parameter = 1 where nilai_parameter like '%$namaFile%'"
        val cursor = db.rawQuery(sql, null)
        val numRow = cursor.count
        cursor.close()
        db.close()
        return numRow > 0
    }

    @Synchronized
    fun refreshDatabase(): Boolean {
        val result: Boolean
        val db = writableDatabase
        result = try {
            refreshReport(db)
            refreshParameter(db)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
        return result
    }

    @Synchronized internal fun refreshReport(db: SQLiteDatabase?): Boolean {
        var database = db
        val result: Boolean
        if (database == null || !database.isOpen)
            database = writableDatabase

        result = try {
            database!!.execSQL("DROP TABLE IF EXISTS report")
            database.execSQL("CREATE TABLE " + "report" + " (id_report TEXT PRIMARY KEY,user_id TEXT, nama_report TEXT, deskripsi_report TEXT, url_report TEXT, tanggal_report TEXT,status_done INT,waktu_report INT)")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        return result
    }

    @Synchronized private fun refreshParameter(db: SQLiteDatabase?): Boolean {
        var database = db
        val result: Boolean
        if (database == null || !database.isOpen)
            database = writableDatabase

        result = try {
            database!!.execSQL("DROP TABLE IF EXISTS parameter")
            database.execSQL("CREATE TABLE " + "parameter" + " (id_parameter TEXT, report_id TEXT,nama_parameter TEXT, nilai_parameter TEXT, tipe_parameter INT, PRIMARY KEY(id_parameter, report_id), FOREIGN KEY(report_id) REFERENCES report(id_report))")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        return result
    }

    @Synchronized
    fun deleteReport(report: GenericReport): Boolean {
        val result: Boolean
        val db = writableDatabase

        result = try {
            db.delete("report", "id_report = ?", arrayOf(report.idReport))
            db.delete("parameter", "report_id = ?", arrayOf(report.idReport))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
        return result
    }

    @Synchronized
    fun addReport(report: GenericReport): Boolean {
        Log.i("DatabaseReport.kt", "Nama Report: " +report.namaReport)
        var result: Boolean
        val db = writableDatabase
        try {
            result = addPartReport(report, db)
            val listParameter = report.parameterList

            if (result) {
                for (parameter in listParameter) {
                    addParameter(parameter, db)
                }
                result = true
            }

        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            db.close()
        }
        return result
    }

    @Synchronized
    fun doneReport(report: GenericReport, done: Int): Boolean {
        val result: Boolean
        val db = writableDatabase

        result = try {
            Log.i("DatabaseReport.kt", "Done Report: UPDATE report set status_done = " + done + " where id_report = " + report.idReport)
            val contentValues = ContentValues()
            contentValues.put("status_done", 1)
            db.update("report", contentValues, "id_report = ?", arrayOf(report.idReport))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }

        return result
    }

    @Synchronized private fun addPartReport(report: GenericReport, db: SQLiteDatabase?): Boolean {
        var database = db

        var result: Boolean
        if (database == null || !database.isOpen)
            database = writableDatabase

        try {
            val sql = "insert into report (" +
                    "id_report ,user_id , nama_report , deskripsi_report, url_report , tanggal_report ,status_done,waktu_report " +
                    ") values (?,?,?, ?,?,?, ?,?)"

            sqlInsert = database!!.compileStatement(sql)
            sqlInsert!!.bindString(1, report.idReport)
            sqlInsert!!.bindString(2, report.user_id)
            sqlInsert!!.bindString(3, report.namaReport)
            sqlInsert!!.bindString(4, report.deskripsiReport)
            sqlInsert!!.bindString(5, report.urlReport)
            sqlInsert!!.bindString(6, report.tanggalReport)
            sqlInsert!!.bindString(7, report.status_done.toString())
            sqlInsert!!.bindString(8, report.waktuReport.toString())
            sqlInsert!!.executeInsert()
            result = true
        } catch (sce: SQLiteConstraintException) {
            sce.printStackTrace()
            result = false
        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            sqlInsert!!.close()
        }

        return result
    }

    @Synchronized private fun addParameter(parameter: ReportParameter, db: SQLiteDatabase?): Boolean {
        var database = db
        var result: Boolean
        if (database == null || !database.isOpen)
            database = writableDatabase

        try {
            val sql = "insert into parameter (" +
                    "id_parameter , report_id ,nama_parameter , nilai_parameter , tipe_parameter " +
                    ") values (?,?,?,?,?)"

            sqlInsert = database!!.compileStatement(sql)
            sqlInsert!!.bindString(1, parameter.idParameter)
            sqlInsert!!.bindString(2, parameter.idReport)
            sqlInsert!!.bindString(3, parameter.namaParameter)
            sqlInsert!!.bindString(4, parameter.valueParameter)
            sqlInsert!!.bindString(5, parameter.tipeParameter.toString())
            sqlInsert!!.executeInsert()
            result = true
        } catch (sce: SQLiteConstraintException) {
            sce.printStackTrace()
            result = false
        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            sqlInsert!!.close()

        }

        return result
    }

    companion object {
        fun getDeskripsi(listReport: List<GenericReport>): Array<String> {
            val daftar = Array(listReport.size, { _ -> "" })

            for (i in listReport.indices) {
                daftar[i] = (i + 1).toString() + ". " + listReport[i].description
            }

            return daftar
        }

        private var databaseReport: DatabaseReport? = null

        @Synchronized
        fun getDatabase(context: Context): DatabaseReport {
            if (databaseReport == null) {
                databaseReport = DatabaseReport(context)
            }

            return databaseReport!!
        }
    }
}