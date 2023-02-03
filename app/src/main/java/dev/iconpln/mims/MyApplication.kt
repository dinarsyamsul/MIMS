package dev.iconpln.mims

import android.util.Log
import androidx.multidex.MultiDexApplication
import dev.iconpln.mims.data.local.database.DaoMaster
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database_local.DatabaseReport
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.StorageUtils
import dev.iconpln.mims.utils.StorageUtils.isPermissionAllowed

class MyApplication : MultiDexApplication(){
    var daoSession: DaoSession? = null

    override fun onCreate() {
        super.onCreate()
        Log.i("PermissionGlobal","${isPermissionAllowed(this)}")
        if(isPermissionAllowed(this)){
            val database = StorageUtils.getDirectory(StorageUtils.DIRECTORY_DATABASE) + "/" + Config.DATABASE_NAME
            val helper = DaoMaster.DevOpenHelper(this, database)
            val db = helper.writableDb
            daoSession = DaoMaster(db).newSession()
            DatabaseReport.getDatabase(this)
        }
    }
}