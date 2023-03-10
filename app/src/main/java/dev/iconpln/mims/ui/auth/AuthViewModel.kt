package dev.iconpln.mims.ui.auth

import android.content.Context
import android.util.Log
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
                        _isLoading.value = false
                        e.printStackTrace()
                    }finally {
                        _isLoading.value = false
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
        daoSession.tPengujianDao.deleteAll()
        daoSession.tPengujianDetailsDao.deleteAll()
        daoSession.tPosDetailDao.deleteAll()
        daoSession.tMaterialGroupsDao.deleteAll()
        daoSession.tPrivilegeDao.deleteAll()
        daoSession.tPosSnsDao.deleteAll()
        daoSession.tPemeriksaanDao.deleteAll()
        daoSession.tPemeriksaanDetailDao.deleteAll()
        daoSession.tPosDetailPenerimaanDao.deleteAll()
        daoSession.tPosPenerimaanDao.deleteAll()
        daoSession.tRatingDao.deleteAll()
        daoSession.tMonitoringPermintaanDao.deleteAll()
        daoSession.tMonitoringPermintaanDetailDao.deleteAll()
        daoSession.tSnMonitoringPermintaanDao.deleteAll()
        daoSession.tPenerimaanUlpDao.deleteAll()
        daoSession.tPenerimaanDetailUlpDao.deleteAll()
        daoSession.tSnPermaterialDao.deleteAll()
        daoSession.tListSnMaterialPenerimaanUlpDao.deleteAll()
        daoSession.tListSnMaterialPemakaianUlpDao.deleteAll()

        if (result != null){

            if (result.pemeriksan != null){
                val size = result.pemeriksan.size
                if (size >0){
                    val items = arrayOfNulls<TPemeriksaan>(size)
                    var item: TPemeriksaan
                    for ((i, model) in result.pemeriksan.withIndex()){
                        item = TPemeriksaan()
                        item.noPemeriksaan = if(model?.noPemeriksaan.isNullOrEmpty()) "" else model?.noPemeriksaan
                        item.storLoc = model?.storLoc
                        item.total = model?.total
                        item.tlskNo = model?.tlskNo
                        item.poSapNo = model?.poSapNo
                        item.poMpNo = model?.poMpNo
                        item.noDoSmar = model?.noDoSmar

                        item.leadTime = model?.leadTime
                        item.createdDate = model?.createdDate
                        item.planCodeNo = model?.plantCodeNo
                        item.plantName = model?.plantName
                        item.noDoMims = model?.noDoMims
                        item.doStatus = model?.doStatus
                        item.statusPemeriksaan = if(model?.statusPemeriksaan.isNullOrEmpty()) "" else model?.statusPemeriksaan

                        item.expeditions = "" //belum perlu ditarik

                        item.courierPersonName = model?.courierPersonName
                        item.kdPabrikan = model?.kdPabrikan
                        item.materialGroup = model?.materialGroup
                        item.namaKategoriMaterial = model?.namaKategoriMaterial

                        item.tanggalDiterima = "" //belum perlu ditarik

                        item.petugasPenerima = model?.petugasPenerima

                        item.namaKurir = model?.courierPersonName
                        item.namaEkspedisi = "" //belum perlu ditarik
                        item.doLineItem = model?.doLineItem
                        item.namaKetua = if (model?.ketuaPemeriksa.isNullOrEmpty()) "" else model?.ketuaPemeriksa
                        item.namaManager = ""
                        item.namaAnggota = ""
                        item.namaSekretaris = ""
                        item.namaAnggotaBaru = ""

                        item.isDone = 0

                        items[i] = item
                    }
                    daoSession.tPemeriksaanDao.insertInTx(items.toList())
                }
            }

            if (result.pemeriksaanDetail != null){
                val size = result.pemeriksaanDetail.size
                if (size >0){
                    val items = arrayOfNulls<TPemeriksaanDetail>(size)
                    var item: TPemeriksaanDetail
                    for ((i, model) in result.pemeriksaanDetail.withIndex()){
                        item = TPemeriksaanDetail()
                        item.noPemeriksaan = if(model?.noPemeriksaan.isNullOrEmpty()) "" else model?.noPemeriksaan
                        item.sn = model?.noSerial
                        item.noDoSmar = model?.noDoSmar
                        item.noMaterail = model?.noMatSap
                        item.noPackaging = model?.noPackaging
                        item.kategori = model?.namaKategoriMaterial
                        item.statusPenerimaan = "" //belum perlu ditarik
                        item.statusPemeriksaan = model?.status

                        if (model?.status == "BELUM DIPERIKSA"){
                            item.isPeriksa = 1
                            item.isComplaint = 0
                            item.isChecked = 0
                        }

                        if (model?.status == "KOMPLAIN"){
                            item.isPeriksa = 0
                            item.isComplaint = 1
                            item.isChecked = 1
                        }

                        if (model?.status.isNullOrEmpty()){
                            item.isPeriksa = 0
                            item.isComplaint = 0
                            item.isChecked = 0
                        }

                        item.isDone = 0

                        items[i] = item
                    }
                    daoSession.tPemeriksaanDetailDao.insertInTx(items.toList())
                }
            }

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
                        item.kodeStatusDoMims = model?.kodeStatusDoMims
                        item.kdPabrikan = model?.kdPabrikan
                        item.materialGroup = model?.materialGroup
                        item.namaKategoriMaterial = model?.namaKategoriMaterial
                        item.noDoMims = model?.noDoMims
                        item.tglDiterima = model?.TglSerahTerima
                        item.kurirPengantar = model?.KurirPengirim
                        item.petugasPenerima = model?.PetugasPenerima
                        item.doLineItem = model?.DoLineItem
                        item.doStatus = model?.doStatus
                        item.expeditions = model?.ekspedition
                        if (model?.ratingDelivery.isNullOrEmpty()) item.ratingDelivery = "" else item.ratingDelivery = model?.ratingDelivery
                        if (model?.ratingQuality.isNullOrEmpty()) item.ratingQuality = "" else item.ratingQuality = model?.ratingQuality
                        if (model?.ratingResponse.isNullOrEmpty()) item.ratingResponse = "" else item.ratingResponse = model?.ratingResponse
                        items[i] = item
                    }
                    daoSession.tPosDao.insertInTx(items.toList())
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
                        item.qtyLolos = model?.qtyLolos
                        item.statusUji = model?.statusUji
                        item.tanggalUji = model?.tglUji.toString()
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
                        item.noPemeriksaan = ""//model?.noPemeriksaan
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
                        item.methodId = model?.methodId
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
                        if(model?.statusPenerimaan.isNullOrEmpty()) item.statusPenerimaan = "" else item.statusPenerimaan = model?.statusPenerimaan
                        if(model?.statusPemeriksaan.isNullOrEmpty()) item.statusPemeriksaan = "" else item.statusPemeriksaan = model?.statusPemeriksaan
                        item.storLoc = model?.storloc
                        item.tglProduksi = model?.tglProduksi
                        item.noPackaging = model?.noPackaging
                        item.doLineItem = model?.doLineItem

                        items[i] = item
                    }
                    daoSession.tPosSnsDao.insertInTx(items.toList())
                }
            }

            if (result.snPermaterial != null) {
                val size = result.snPermaterial.size
                if (size > 0) {
                    val items = arrayOfNulls<TSnPermaterial>(size)
                    var item: TSnPermaterial
                    for ((i, model) in result.snPermaterial.withIndex()){
                        item = TSnPermaterial()
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
                        if(model?.status.isNullOrEmpty()) item.status = "" else item.status = model?.status
                        item.storLoc = model?.storloc
                        item.tglProduksi = model?.tglProduksi
                        item.noPackaging = model?.noPackaging
                        item.doLineItem = model?.doLineItem

                        items[i] = item
                    }
                    daoSession.tSnPermaterialDao.insertInTx(items.toList())
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

            if (result.ratings != null){
                val size = result.ratings.size
                if (size > 0) {
                    val items = arrayOfNulls<TRating>(size)
                    var item: TRating
                    for ((i, model) in result.ratings.withIndex()){
                        item = TRating()
                        item.kdRating = model?.kdRating
                        item.nilai = model?.nilai
                        item.keterangan = model?.keterangan
                        item.isActive = 0
                        when (model?.kdRating) {
                            "11", "12", "13", "14", "15" -> {
                                item.type = "Kualitas Penerimaan"
                            }
                            "21", "22", "23", "24", "25" -> {
                                item.type = "Waktu Pengiriman"
                            }
                            "31", "32", "33", "34", "35" -> {
                                item.type = "Respon Penyedia"
                            }
                        }

                        items[i] = item
                    }
                    daoSession.tRatingDao.insertInTx(items.toList())
                }
            }

            if (result.monitoringPermintaan != null){
                val size = result.monitoringPermintaan.size
                if (size > 0) {
                    val items = arrayOfNulls<TMonitoringPermintaan>(size)
                    var item: TMonitoringPermintaan
                    for ((i, model) in result.monitoringPermintaan.withIndex()){
                        item = TMonitoringPermintaan()
                        item.createdDate = model?.createdDate
                        item.plant = model?.plant
                        item.plantName = model?.plantName
                        item.noTransaksi = model?.noTransaksi
                        item.createdBy = model?.createdBy
                        item.jumlahKardus = model?.jumlahKardus ?: 0
                        item.kodePengeluaran = model?.kodePengeluaran.toString()
                        item.noPermintaan = model?.noPermintaan
                        item.noRepackaging = model?.noRepackaging
                        item.storLocAsal = model?.storLocAsal
                        item.storLocAsalName = model?.storLocAsalName
                        item.storLocTujuan = model?.storLocTujuan
                        item.storLocTujuanName = model?.storLocTujuanName
                        item.tanggalPengeluaran = model?.tanggalPengeluaran.toString()
                        item.tanggalPermintaan = model?.tanggalPermintaan
                        item.updatedBy = model?.updatedBy
                        item.updatedDate = model?.updatedDate

                        items[i] = item
                    }
                    daoSession.tMonitoringPermintaanDao.insertInTx(items.toList())
                }
            }

            if (result.monitoringPermintaanDetails != null){
                val size = result.monitoringPermintaanDetails.size
                if (size > 0) {
                    val items = arrayOfNulls<TMonitoringPermintaanDetail>(size)
                    var item: TMonitoringPermintaanDetail
                    for ((i, model) in result.monitoringPermintaanDetails.withIndex()){
                        item = TMonitoringPermintaanDetail()
                        item.unit = model?.unit
                        item.nomorMaterial = model?.nomorMaterial
                        item.kategori = model?.kategori
                        item.materialDesc = model?.materialDesc
                        item.noPermintaan = model?.noPermintaan
                        item.noTransaksi = model?.noTransaksi
                        item.noRepackaging = model?.noRepackaging
                        item.qtyPengeluaran = model?.qtyPengeluaran.toString()
                        item.qtyPermintaan = model?.qtyPermintaan ?: 0
                        item.qtyScan = model?.qtyScan.toString()

                        items[i] = item
                    }
                    daoSession.tMonitoringPermintaanDetailDao.insertInTx(items.toList())
                }
            }

            if (result.penerimaanUlp != null){
                val size = result.penerimaanUlp.size
                if (size > 0) {
                    val items = arrayOfNulls<TPenerimaanUlp>(size)
                    var item: TPenerimaanUlp
                    for ((i, model) in result.penerimaanUlp.withIndex()){
                        item = TPenerimaanUlp()
                        item.noPengiriman = model?.noPengiriman
                        item.noPermintaan = model?.noPermintaan
                        item.statusPemeriksaan = model?.statusPemeriksaan
                        item.deliveryDate = model?.tanggalPengiriman
                        item.statusPenerimaan = model?.statusPenerimaan
                        item.jumlahKardus = model?.jumlahKardus
                        item.gudangAsal = model?.storLocAsalName
                        item.noRepackaging = model?.noRepackaging
                        item.gudangTujuan = model?.storLocTujuanName
                        item.tanggalPemeriksaan = model?.tanggalPemeriksaan
                        item.tanggalPenerimaan = model?.tanggalPenerimaan
                        item.kepalaGudangPemeriksa = model?.kepalaGudang
                        item.pejabatPemeriksa = model?.namaPemeriksa1
                        item.jabatanPemeriksa = model?.jabatanPemeriksa1
                        item.namaPetugasPemeriksa = model?.namaPemeriksa2
                        item.jabatanPetugasPemeriksa = model?.jabatanPemeriksa2
                        item.kepalaGudangPenerima = model?.kepalaGudang
                        item.noPk = model?.noPk
                        item.tanggalDokumen = model?.tanggalDokumen
                        item.pejabatPenerima = model?.pejabatPenerima
                        item.kurir = model?.kurir
                        item.noNota = model?.noNota
                        item.noMaterial = model?.nomorMaterial
                        item.spesifikasi = model?.materialDesc
                        item.kuantitasPeriksa = model?.qtyPemeriksaan
                        item.kuantitas = model?.qtyPenerimaan
                        items[i] = item
                    }
                    daoSession.tPenerimaanUlpDao.insertInTx(items.toList())
                }
            }

            if (result.penerimaanDetailUlp != null){
                val size = result.penerimaanDetailUlp.size
                if (size > 0) {
                    val items = arrayOfNulls<TPenerimaanDetailUlp>(size)
                    var item: TPenerimaanDetailUlp
                    for ((i, model) in result.penerimaanDetailUlp.withIndex()){
                        item = TPenerimaanDetailUlp()
                        item.noRepackaging = model?.noRepackaging
                        item.noTransaksi = model?.noTransaksi
                        item.qtyPenerimaan = model?.qtyPenerimaan
                        item.materialDesc = model?.materialDesc
                        item.noMaterial = model?.nomorMaterial
                        item.qtyPemeriksaan = model?.qtyPemeriksaan
                        item.qtyPengiriman = model?.qtyPengiriman
                        item.qtyPermintaan = model?.qtyPermintaan
                        item.qtySesuai = model?.qtySesuai
                        items[i] = item
                    }
                    daoSession.tPenerimaanDetailUlpDao.insertInTx(items.toList())
                }
            }

            if (result.snPermintaan != null){
                val size = result.snPermintaan.size
                if (size > 0) {
                    val items = arrayOfNulls<TSnMonitoringPermintaan>(size)
                    var item: TSnMonitoringPermintaan
                    for ((i, model) in result.snPermintaan.withIndex()){
                        item = TSnMonitoringPermintaan()

                        item.noRepackaging = model?.noRepackaging
                        item.nomorMaterial = model?.nomorMaterial
                        item.serialNumber = model?.serialNumber
                        item.status = if (model?.status.isNullOrEmpty()) "" else model?.status
                        items[i] = item
                    }
                    daoSession.tSnMonitoringPermintaanDao.insertInTx(items.toList())
                }
            }

            if (result.pemakaian != null){
                val size = result.pemakaian.size
                if (size > 0) {
                    val items = arrayOfNulls<TPemakaian>(size)
                    var item: TPemakaian
                    for ((i, model) in result.pemakaian.withIndex()){
                        item = TPemakaian()

                        item.plant = model?.plant
                        item.storLoc = model?.storLoc
                        item.daya = model?.daya
                        item.noTransaksi = model?.noTransaksi
                        item.alamatPelanggan = model?.alamatPelanggan
                        item.idPelanggan = model?.idPelanggan
                        item.jenisPekerjaan = model?.jenisPekerjaan
                        item.kodeIntegrasi = model?.kodeIntegrasi
                        item.namaPelanggan = model?.namaPelanggan
                        item.noAgenda = model?.noAgenda
                        item.noPemesanan = model?.noPemesanan
                        item.noReservasi = model?.noReservasi
                        item.statusKirimAgo = model?.statusKirimAgo.toString()
                        item.statusPemakaian = model?.statusPemakaian
                        item.daya = model?.daya
                        item.alamatPelanggan = model?.alamatPelanggan
                        item.statusSap = model?.statusSap.toString()
                        item.noTransaksi = model?.noTransaksi
                        item.sumber = model?.sumber
                        item.tanggalBayar = model?.tanggalBayar.toString()
                        item.tanggalDokumen = model?.tanggalDokumen
                        item.tanggalPemakaian = model?.tanggalPemakaian
                        item.tanggalPengeluaran = model?.tanggalPengeluaran
                        item.tanggalReservasi = model?.tanggalReservasi
                        item.tarif = model?.tarif
                        items[i] = item
                    }
                    daoSession.tPemakaianDao.insertInTx(items.toList())
                }
            }

            if (result.pemakaianDetail != null){
                val size = result.pemakaianDetail.size
                if (size > 0) {
                    val items = arrayOfNulls<TPemakaianDetail>(size)
                    var item: TPemakaianDetail
                    for ((i, model) in result.pemakaianDetail.withIndex()){
                        item = TPemakaianDetail()

                        item.nomorMaterial = model?.nomorMaterial
                        item.noTransaksi = model?.noTransaksi
                        item.unit = model?.unit
                        item.keterangan = model?.keterangan
                        item.namaMaterial = model?.namaMaterial
                        item.noMeter = model?.noMeter
                        item.qtyPemakaian = model?.qtyPemakaian.toString()
                        item.qtyPengeluaran = model?.qtyPengeluaran.toString()
                        item.qtyReservasi = model?.qtyReservasi.toString()
                        item.valuationType = model?.valuationType
                        items[i] = item
                    }
                    daoSession.tPemakaianDetailDao.insertInTx(items.toList())
                }
            }

            if (result.snPenerimaanUlp != null){
                val size = result.snPenerimaanUlp.size
                if (size > 0) {
                    val items = arrayOfNulls<TListSnMaterialPenerimaanUlp>(size)
                    var item: TListSnMaterialPenerimaanUlp
                    for ((i, model) in result.snPenerimaanUlp.withIndex()){
                        item = TListSnMaterialPenerimaanUlp()
                        item.status = ""
                        item.noRepackaging = model?.noRepackaging
                        item.noMaterial = model?.nomorMaterial
                        item.noSerialNumber = model?.serialNumber
                        item.isScanned = 0
                        items[i] = item
                    }
                    daoSession.tListSnMaterialPenerimaanUlpDao.insertInTx(items.toList())
                }
            }

            if (result.snPenerimaanUlp != null){
                val size = result.snPenerimaanUlp.size
                if (size > 0) {
                    val items = arrayOfNulls<TListSnMaterialPenerimaanUlp>(size)
                    var item: TListSnMaterialPenerimaanUlp
                    for ((i, model) in result.snPenerimaanUlp.withIndex()){
                        item = TListSnMaterialPenerimaanUlp()
                        item.status = ""
                        item.noRepackaging = model?.noRepackaging
                        item.noMaterial = model?.nomorMaterial
                        item.noSerialNumber = model?.serialNumber
                        item.isScanned = 0
                        items[i] = item
                    }
                    daoSession.tListSnMaterialPenerimaanUlpDao.insertInTx(items.toList())
                }
            }

            if (result.snPemakaianUlp != null){
                val size = result.snPemakaianUlp.size
                if (size > 0) {
                    val items = arrayOfNulls<TListSnMaterialPemakaianUlp>(size)
                    var item: TListSnMaterialPemakaianUlp
                    for ((i, model) in result.snPemakaianUlp.withIndex()){
                        item = TListSnMaterialPemakaianUlp()
                        item.noTransaksi = model?.noTransaksi
                        item.noMaterial = model?.nomorMaterial
                        item.noSerialNumber = model?.serialNumber
                        item.isScanned = 0
                        items[i] = item
                    }
                    daoSession.tListSnMaterialPemakaianUlpDao.insertInTx(items.toList())
                }
            }
        }
    }
}