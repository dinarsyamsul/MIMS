package dev.iconpln.mims.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import dev.iconpln.mims.data.local.database_local.GenericReport
import dev.iconpln.mims.data.local.databasereport.DatabaseReport

class TambahReportTask<T : Context> : AsyncTask<GenericReport, String, String> where T : Loadable {
    internal var context: T
    private var listReport: List<GenericReport>? = null

    constructor(ctx: T) {
        context = ctx
    }

    constructor(ctx: T, list: List<GenericReport>) {
        context = ctx
        listReport = list
    }

    override fun onPreExecute() {
        super.onPreExecute()
        context.setLoading(true, "Loading", "Mohon tunggu...")
    }

    override fun doInBackground(vararg params: GenericReport): String {
        val sender = DatabaseReport.getDatabase(context)
        var jumlahReport = 0

        Log.i("TambahReportTask.kt", "Jumlah Report: " + params.size)
        var result = OK
        for (i in params.indices) {
            val report = params[i]
            if (report != null) {
                publishProgress("Menambahkan " + report.namaReport)
                Log.i("TambahReportTask.kt", "Nama Report: " + report.namaReport)
                val sending = sender.addReport(report)
                if (sending) {
                    jumlahReport++
                } else {
                    result = FAIL
                    break
                }
            }
        }

        if (FAIL == result) {
            for (report in params) {
                if (report != null) {
                    sender.deleteReport(report)
                }
            }
        }

        if (listReport != null && OK == result) {
            for (report in listReport!!) {
                if (report != null) {
                    publishProgress("Menambahkan " + report.namaReport)
                    val sending = sender.addReport(report)
                    if (sending) {
                        jumlahReport++
                    } else {
                        result = FAIL
                        break
                    }
                }
            }

            if (FAIL == result) {
                for (report in listReport!!) {
                    if (report != null) {
                        sender.deleteReport(report)
                    }
                }
            }
        }
        return result + ";" + jumlahReport.toString()
    }

    //    void deleteReport(DatabaseReport db, GenericReport report)
    //    {
    //        db.deleteReport(report);
    //    }

    override fun onProgressUpdate(vararg values: String) {
        super.onProgressUpdate(*values)
        context.setLoading(true, "Loading", values[0])
    }

    override fun onPostExecute(value: String) {
        Log.i("TambahReportTask.kt", "Value: " + value);
        super.onPostExecute(value)
        context.setLoading(false, "", "")
        val hasil = value.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (hasil[0] == OK) {
            context.setFinish(true, hasil[1] + " data report berhasil ditambahkan")
        } else {
            context.setFinish(
                hasil[0] == OK,
                "Terjadi error saat menyimpan report, silahkan coba lagi!"
            )
        }
    }

    companion object {
        const val OK = "OK"
        const val FAIL = "FAIL"
    }
}
