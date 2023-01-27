package dev.iconpln.mims.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.remote.response.AgoLoginResponse
import dev.iconpln.mims.data.remote.response.HitEmailResponse
import dev.iconpln.mims.data.remote.response.LoginResponse
import dev.iconpln.mims.data.remote.response.VerifyTokenResponse
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.utils.Config
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.*


class LoginViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponses = MutableLiveData<String>()
    val loginResponses: LiveData<String> = _loginResponses

    private val _agoLoginResponse = MutableLiveData<AgoLoginResponse>()
    val agoLoginResponse: LiveData<AgoLoginResponse> = _agoLoginResponse

    private val _hitEmailResponse = MutableLiveData<HitEmailResponse>()
    val hitEmailResponse: LiveData<HitEmailResponse> = _hitEmailResponse

    private val _verifyTokenResponse = MutableLiveData<VerifyTokenResponse>()
    val verifyTokenResponse: LiveData<VerifyTokenResponse> = _verifyTokenResponse

    fun getLogin(username: String, password: String, device_token: String, mAndroidId: String,
                 mAppVersion: String,mDeviceData: String,mIpAddress: String,androidVersion: Int,dateTimeUtc: Long,ctx: Context, daoSession: DaoSession) {
        _isLoading.value = true
        val session = SessionManager(ctx)
        val apiService = ApiConfig.getApiService(ctx)
        CoroutineScope(Dispatchers.IO).launch {
            val requestBody = mutableMapOf<String, String>()
            requestBody["username"] = username
            requestBody["password"] = "12345"
            requestBody["device_token"] = device_token
            requestBody["android_id"] = mAndroidId
            requestBody["app_version"] = mAppVersion
            requestBody["device_data"] = mDeviceData
            requestBody["datetime_utc"] = dateTimeUtc.toString()
            requestBody["ip_address"] = mIpAddress
            requestBody["android_version"] = androidVersion.toString()
            val response = apiService.login(requestBody)

            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        val loginResult = response.body()
                        _loginResponses.value = loginResult?.message!!
                        insertToDbLocal(daoSession,loginResult)
                        session.sessionActivity(Config.SESSION_ACTIVITY_DASHBOARD_PABRIKAN)
                        session.saveAuthToken(loginResult.token.toString(), loginResult.user?.roleId.toString())
                    } else {
                        _isLoading.value = false
                    }
                }catch (e: Exception){
                    Log.d("checkLogin", e.toString())
                }
            }
        }
    }

    private fun insertToDbLocal(daoSession: DaoSession, result: LoginResponse) {
        daoSession.tLokasiDao.deleteAll()
        daoSession.tMaterialDao.deleteAll()
        daoSession.tMaterialDetailDao.deleteAll()
        daoSession.tPosDao.deleteAll()
        daoSession.tPengujianDao.deleteAll()
        daoSession.tPengujianDetailsDao.deleteAll()
        daoSession.tPosDetailDao.deleteAll()
        daoSession.tMaterialGroupsDao.deleteAll()
        daoSession.tPrivilegeDao.deleteAll()
        daoSession.tPosSnsDao.deleteAll()

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

            if (result.pos != null){
                val size = result.pos.size
                if (size > 0) {
                    val items = arrayOfNulls<TPosPenerimaan>(size)
                    var item: TPosPenerimaan
                    for ((i, model) in result.pos.withIndex()){
                        item = TPosPenerimaan()
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
                        item.tanggalDiterima = ""
                        item.photoBarang = ""
                        item.photoSuratBarang = ""
                        item.namaKurir = ""
                        item.namaEkspedisi = ""
                        item.petugasPenerima = ""
                        items[i] = item
                    }
                    daoSession.tPosPenerimaanDao.insertInTx(items.toList())
                }
            }

            if (result.pengujians != null){
                val size = result.pengujians.size
                if (size > 0) {
                    val items = arrayOfNulls<TPengujian>(size)
                    var item: TPengujian
                    for ((i, model) in result.pengujians.withIndex()){
                        item = TPengujian()
                        item.kdPabrikan = model?.kdPabrikan
                        item.namaKategori = model?.namaKategori
                        item.noPengujian = model?.noPengujian
                        item.qtyMaterial = model?.qtyMaterial
                        item.qtySiap = model?.qtySiap
                        item.statusUji = model?.statusUji
                        item.tanggalUji = model?.tanggalUji
                        item.unit = model?.unit
                        items[i] = item
                    }
                    daoSession.tPengujianDao.insertInTx(items.toList())
                }
            }

            if (result.pengujianDetails != null){
                val size = result.pengujianDetails.size
                if (size > 0) {
                    val items = arrayOfNulls<TPengujianDetails>(size)
                    var item: TPengujianDetails
                    for ((i, model) in result.pengujianDetails.withIndex()){
                        item = TPengujianDetails()
                        item.serialNumber = model?.serialNumber
                        item.namaKategori = model?.namaKategori
                        item.noPengujian = model?.noPengujian
                        item.statusUji = model?.statusUji
                        item.keteranganMaterial = model?.keteranganMaterial
                        items[i] = item
                    }
                    daoSession.tPengujianDetailsDao.insertInTx(items.toList())
                }
            }

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
                        item.noPemeriksaan = model?.noPemeriksaan
                        items[i] = item
                    }
                    daoSession.tPosDetailDao.insertInTx(items.toList())
                }
            }

            if (result.posDetail != null){
                val size = result.posDetail.size
                if (size > 0) {
                    val items = arrayOfNulls<TPosDetailPenerimaan>(size)
                    var item: TPosDetailPenerimaan
                    for ((i, model) in result.posDetail.withIndex()){
                        item = TPosDetailPenerimaan()
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
                        item.noPemeriksaan = model?.noPemeriksaan
                        items[i] = item
                    }
                    daoSession.tPosDetailPenerimaanDao.insertInTx(items.toList())
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
        }
    }

//    fun hitEmail(username: String) {
//        _isLoading.value = true
//        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            val requestBody = mutableMapOf<String, String>()
//            requestBody["username"] = username
//            val response = apiService.hitEmailResponse(requestBody)
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//                    _isLoading.value = false
//                    val hitMailResult = response.body()
//                    _hitEmailResponse.postValue(hitMailResult!!)
//                } else {
//                    _isLoading.value = false
//                    val error = response.errorBody()?.toString()
//                    onError("Error : ${error?.let { getErrorMessage(it) }}")
//                }
//            }
//        }
//    }

//    fun sendTokenOtp(otp: String, username: String, androidId: String, deviceData: String) {
//        _isLoading.value = true
//        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            val requestBody = mutableMapOf<String, String>()
////            requestBody["email_token"] = mail_token
//            requestBody["username"] = username
//            requestBody["otp"] = otp
//            requestBody["android_id"] = androidId
//            requestBody["device_data"] = deviceData
//            val response = apiService.sendOtp(requestBody)
//
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//                    _isLoading.value = false
//                    val tokenResult = response.body()
//                    _verifyTokenResponse.postValue(tokenResult!!)
//                    session.saveAuthToken(
//                        user_token = tokenResult?.token.toString(),
//                        device_token = "",
//                        role_id = tokenResult?.data?.roleId.toString()
//                    )
//
//                    session.sessionActivity(Config.SESSION_ACTIVITY_DASHBOARD_PABRIKAN)
//                } else {
//                    _isLoading.value = false
//                    val error = response.errorBody()?.toString()
//                    onError("Error : ${error?.let { getErrorMessage(it) }}")
//                }
//            }
//        }
//    }
}