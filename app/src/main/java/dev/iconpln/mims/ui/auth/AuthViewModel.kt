package dev.iconpln.mims.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.remote.response.GenericResponse
import dev.iconpln.mims.data.remote.response.LoginResponse
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.*

class AuthViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _checkOtpForgotPassword = MutableLiveData<GenericResponse>()
    val checkOtpForgotPassword: LiveData<GenericResponse> = _checkOtpForgotPassword

    private val _changePassword = MutableLiveData<GenericResponse>()
    val changePassword: LiveData<GenericResponse> = _changePassword

    fun getLogin(context: Context, daoSession: DaoSession, username: String, password: String, device_token: String,
                 mAndroidId: String, mAppVersion: String, mDeviceData: String, mIpAddress: String,
                 androidVersion: Int, dateTimeUtc: Long,session: SessionManager) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            requestBody["password"] = password
            requestBody["device_token"] = device_token
            requestBody["android_id"] = mAndroidId
            requestBody["app_version"] = "1.0.0"
            requestBody["device_data"] = mDeviceData
            requestBody["datetime_utc"] = dateTimeUtc.toString()
            requestBody["ip_address"] = mIpAddress
            requestBody["android_version"] = androidVersion.toString()

            val response = ApiConfig.getApiService(context).login(requestBody)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        _isLoading.value = false
                        val loginResult = response.body()
                        _loginResponse.postValue(loginResult!!)
                        session.saveUsernamePassword(username,password)
                        inserToDbLocal(daoSession, loginResult)

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    _isLoading.value = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun sendOtp(context: Context, username: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username

            val response = ApiConfig.getApiService(context).sendOtp(requestBody)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        _isLoading.value = false
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    _isLoading.value = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun checkOtp(context: Context, username: String,otp: String, androidId: String, deviceData: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            requestBody["otp"] = otp
            requestBody["android_id"] = androidId
            requestBody["device_data"] = deviceData

            val response = ApiConfig.getApiService(context).otpValid(requestBody)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        _isLoading.value = false
                        val responses = response.body()
                        _loginResponse.postValue(responses!!)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    _isLoading.value = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun sendOtpForgotPassword(context: Context, username: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username

            val response = ApiConfig.getApiService(context).getOtpForgotPassword(requestBody)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        _isLoading.value = false
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    _isLoading.value = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun checkOtpForgotPassword(context: Context, username: String,otp: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            requestBody["otp"] = otp

            val response = ApiConfig.getApiService(context).otpValidForgotPassword(requestBody)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        _isLoading.value = false
                        val responses = response.body()
                        _checkOtpForgotPassword.postValue(responses!!)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    _isLoading.value = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun changePassword(context: Context, username: String,password: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            requestBody["new_password"] = password

            val response = ApiConfig.getApiService(context).changePassword(requestBody)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    try {
                        _isLoading.value = false
                        val responses = response.body()
                        _changePassword.postValue(responses!!)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    _isLoading.value = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun inserToDbLocal(daoSession: DaoSession, result: LoginResponse) {
        daoSession.tLokasiDao.deleteAll()
        daoSession.tMaterialDao.deleteAll()
        daoSession.tMaterialDetailDao.deleteAll()
        daoSession.tPosDao.deleteAll()
//        daoSession.tPengujianDao.deleteAll()
//        daoSession.tPengujianDetailsDao.deleteAll()
        daoSession.tPosDetailDao.deleteAll()
        daoSession.tMaterialGroupsDao.deleteAll()
        daoSession.tPrivilegeDao.deleteAll()
        daoSession.tPosSnsDao.deleteAll()
        daoSession.tLokasiDao.deleteAll()

        if (result != null){
            if (result.materialDetails != null){
                val size = result.materialDetails.size
                if (size > 0) {
                    val items = arrayOfNulls<TMaterialDetail>(size)
                    var item: TMaterialDetail
                    for ((i, model) in result.materialDetails.withIndex()){
                        item = TMaterialDetail()
                        item.kdPabrikan = model?.kdPabrikan
                        item.masaGaransi = model?.masaGaransi
                        item.mmc = model?.mmc
                        item.materialId = model?.materialId.toString()
                        item.materialGroup = model?.materialGroup.toString()
                        item.noPackaging = model?.noPackaging.toString()
                        item.noProduksi = model?.noProduksi
                        item.nomorMaterial = model?.nomorMaterial
                        item.nomorSertMaterologi = model?.nomorSertMaterologi
                        item.plant = model?.plant.toString()
                        item.serialNumber = model?.serialNumber
                        item.spesifikasi = model?.spesifikasi
                        item.spln = model?.spln
                        item.storloc = model?.storloc.toString()
                        item.tglProduksi = model?.tglProduksi
                        item.namaKategoriMaterial = model?.namaKategoriMaterial
                        item.tahun = model?.tahun.toString()
                        items[i] = item
                    }
                    daoSession.tMaterialDetailDao.insertInTx(items.toList())
                }
            }

            if (result.materials != null){
                val size = result.materials.size
                if (size > 0) {
                    val items = arrayOfNulls<TMaterial>(size)
                    var item: TMaterial
                    for ((i, model) in result.materials.withIndex()){
                        item = TMaterial()
                        item.masaGaransi = model?.masaGaransi
                        item.mmc = model?.mmc
                        item.materialId = model?.materialId.toString()
                        item.materialGroup = model?.materialGroup.toString()
                        item.nomorMaterial = model?.nomorMaterial
                        item.nomorSertMaterologi = model?.nomorSertMaterologi
                        item.plant = model?.plant.toString()
                        item.storloc = model?.storloc.toString()
                        item.tglProduksi = model?.tglProduksi
                        item.noProduksi = model?.noProduksi
                        item.kdPabrikan = model?.kdPabrikan
                        item.namaKategoriMaterial = model?.namaKategoriMaterial
                        item.tahun = model?.tahun.toString()
                        items[i] = item
                    }
                    daoSession.tMaterialDao.insertInTx(items.toList())
                }
            }

            if (result.pos != null){
                val size = result.pos.size
                if (size > 0) {
                    val items = arrayOfNulls<TPos>(size)
                    var item: TPos
                    for ((i, model) in result.pos.withIndex()){
                        item = TPos()
                        item.createdDate = model?.createdDate
                        item.leadTime = model?.leadTime
                        item.storloc = model?.storLoc
                        item.noDoSmar = model?.noDoSmar
                        item.planCodeNo = model?.plantCodeNo
                        item.plantName = model?.plantName
                        item.poMpNo = model?.poMpNo
                        item.poSapNo = model?.poSapNo
                        item.storLoc = model?.storLoc
                        item.tlskNo = model?.tlskNo
                        item.total = model?.total
                        item.kdPabrikan = model?.kdPabrikan
                        item.materialGroup = model?.materialGroup
                        item.namaKategoriMaterial = model?.namaKategoriMaterial
                        item.noDoMims = model?.noDoMims
                        items[i] = item
                    }
                    daoSession.tPosDao.insertInTx(items.toList())
                }
            }

//            if (result.pengujians != null){
//                val size = result.pengujians.size
//                if (size > 0) {
//                    val items = arrayOfNulls<TPengujian>(size)
//                    var item: TPengujian
//                    for ((i, model) in result.pengujians.withIndex()){
//                        item = TPengujian()
//                        item.kdPabrikan = model?.kdPabrikan
//                        item.namaKategori = model?.namaKategori
//                        item.noPengujian = model?.noPengujian
//                        item.qtyMaterial = model?.qtyMaterial
//                        item.qtySiap = model?.qtySiap
//                        item.statusUji = model?.statusUji
//                        item.tanggalUji = model?.tanggalUji.toString()
//                        item.unit = model?.unit
//                        items[i] = item
//                    }
//                    daoSession.tPengujianDao.insertInTx(items.toList())
//                }
//            }

//            if (result.pengujianDetails != null){
//                val size = result.pengujianDetails.size
//                if (size > 0) {
//                    val items = arrayOfNulls<TPengujianDetails>(size)
//                    var item: TPengujianDetails
//                    for ((i, model) in result.pengujianDetails.withIndex()){
//                        item = TPengujianDetails()
//                        item.serialNumber = model?.serialNumber
//                        item.namaKategori = model?.namaKategori
//                        item.noPengujian = model?.noPengujian
//                        item.statusUji = model?.statusUji
//                        item.keteranganMaterial = model?.keteranganMaterial
//                        items[i] = item
//                    }
//                    daoSession.tPengujianDetailsDao.insertInTx(items.toList())
//                }
//            }

            if (result.posDetail != null){
                val size = result.posDetail.size
                if (size > 0) {
                    val items = arrayOfNulls<TPosDetail>(size)
                    var item: TPosDetail
                    for ((i, model) in result.posDetail.withIndex()){
                        item = TPosDetail()
                        item.doStatus = model?.doStatus
                        item.kdPabrikan = model?.kdPabrikan
                        item.noDoSmar = model?.noDoSmar
                        item.noPackaging = model?.noPackaging
                        item.noMatSap = model?.noMatSap
                        item.qty = model?.qty
                        item.leadTime = model?.leadTime.toString()
                        item.noDoMims = model?.noDoMims
                        item.plantCodeNo = model?.plantCodeNo
                        item.poMpNo = model?.poMpNo
                        item.plantName = model?.plantName
                        item.poSapNo = model?.poSapNo
                        item.storLoc = model?.storLoc
                        item.uom = model?.uom
                        item.createdDate = model?.createdDate
                        item.noPemeriksaan = model?.noPemeriksaan.toString()
                        items[i] = item
                    }
                    daoSession.tPosDetailDao.insertInTx(items.toList())
                }
            }

            if (result.materialGroups != null){
                val size = result.materialGroups.size
                if (size > 0) {
                    val items = arrayOfNulls<TMaterialGroups>(size)
                    var item: TMaterialGroups
                    for ((i, model) in result.materialGroups.withIndex()){
                        item = TMaterialGroups()
                        item.materialGroup = model?.materialGroup
                        item.namaKategoriMaterial = model?.namaKategoriMaterial
                        items[i] = item
                    }
                    daoSession.tMaterialGroupsDao.insertInTx(items.toList())
                }
            }

            if (result.privilege != null) {
                val size = result.privilege.size
                if (size > 0) {
                    val items = arrayOfNulls<TPrivilege>(size)
                    var item: TPrivilege
                    for ((i, model) in result.privilege.withIndex()){
                        item = TPrivilege()
                        item.isActive = model?.isActive.toString()
                        item.methodId = model?.methodValue
                        item.methodValue = model?.methodValue
                        item.moduleId = model?.moduleId
                        item.roleId = model?.roleId.toString()
                        items[i] = item
                    }
                    daoSession.tPrivilegeDao.insertInTx(items.toList())
                }
            }

            if (result.posSns != null) {
                val size = result.posSns.size
                if (size > 0) {
                    val items = arrayOfNulls<TPosSns>(size)
                    var item: TPosSns
                    for ((i, model) in result.posSns.withIndex()){
                        item = TPosSns()
                        item.doStatus = model?.doStatus
                        item.kdPabrikan = model?.kdPabrikan
                        item.masaGaransi = model?.masaGaransi
                        item.mmc = model?.mmc
                        item.materialId = model?.materialId
                        item.namaKategoriMaterial = model?.namaKategoriMaterial
                        item.noDoSmar = model?.noDoSmar
                        item.noMatSap = model?.noMatSap
                        item.noProduksi = model?.noProduksi
                        item.noSerial = model?.noSerial
                        item.noSertMeterologi = model?.nomorSertMaterologi
                        item.plant = model?.plant
                        item.spesifikasi = model?.spesifikasi
                        item.spln = model?.spln
                        item.status = model?.status.toString()
                        item.storLoc = model?.storloc
                        item.tglProduksi = model?.tglProduksi

                        items[i] = item
                    }
                    daoSession.tPosSnsDao.insertInTx(items.toList())
                }
            }

            if (result.lokasis != null) {
                val size = result.lokasis.size
                if (size > 0) {
                    val items = arrayOfNulls<TLokasi>(size)
                    var item: TLokasi
                    for ((i, model) in result.lokasis.withIndex()){
                        item = TLokasi()
                        item.noDoSns = model?.noDoMims
                        item.ket = model?.ket
                        item.updateDate = model?.updatedDate

                        items[i] = item
                    }
                    daoSession.tLokasiDao.insertInTx(items.toList())
                }
            }
        }
    }
}