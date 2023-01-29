package dev.iconpln.mims

import android.content.pm.PackageManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import dev.iconpln.mims.data.local.database.DaoMaster
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.databasereport.DatabaseReport
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.StorageUtils

class MyApplication : MultiDexApplication(){
    var daoSession: DaoSession? = null

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                StorageUtils.createDirectories()
                val database = StorageUtils.getDirectory(StorageUtils.DIRECTORY_DATABASE) + "/" + Config.DATABASE_NAME
                val helper = DaoMaster.DevOpenHelper(this, database)
                val db = helper.writableDb
                daoSession = DaoMaster(db).newSession()
                DatabaseReport.getDatabase(this)
            }
            // Otherwise, call DaoSession on LoginActivity
        } else {
            StorageUtils.createDirectories()
            val database = StorageUtils.getDirectory(StorageUtils.DIRECTORY_DATABASE) + "/" + Config.DATABASE_NAME
            val helper = DaoMaster.DevOpenHelper(this, database)
            val db = helper.writableDb
            daoSession = DaoMaster(db).newSession()
            DatabaseReport.getDatabase(this)
        }
    }
}