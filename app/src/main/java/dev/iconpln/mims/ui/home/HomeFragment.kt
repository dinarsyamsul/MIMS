package dev.iconpln.mims.ui.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.iconpln.mims.HomeActivity
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.remote.response.LoginResponse
import dev.iconpln.mims.data.remote.service.ApiConfig
import dev.iconpln.mims.databinding.FragmentHomeBinding
import dev.iconpln.mims.ui.monitoring.MonitoringActivity
import dev.iconpln.mims.ui.pengiriman.PengirimanActivity
import dev.iconpln.mims.ui.arttribute_material.DataAtributMaterialActivity
import dev.iconpln.mims.ui.monitoring_permintaan.MonitoringPermintaanActivity
import dev.iconpln.mims.ui.pemakaian.PemakaianActivity
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pengujian.PengujianActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.ui.pnerimaan.approval.ApprovalActivity
import dev.iconpln.mims.ui.pnerimaan.registrasi.RegistrasiSnMaterialActivity
import dev.iconpln.mims.ui.tracking.TrackingHistoryActivity
import dev.iconpln.mims.ui.ulp.penerimaan.PenerimaanUlpActivity
import dev.iconpln.mims.utils.Helper
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTimeUtils

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog : Dialog
    private lateinit var daoSession: DaoSession

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        daoSession = (requireActivity().application as MyApplication).daoSession!!
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nilaiPenerimaanUlp = daoSession.tPenerimaanUlpDao.queryBuilder()
            .whereOr(TPenerimaanUlpDao.Properties.StatusPenerimaan.notEq("DITERIMA"),
                TPenerimaanUlpDao.Properties.StatusPemeriksaan.notEq("SUDAH DIPERIKSA")).list().size.toString()

        val nilaiMaterial = daoSession.tMaterialDao.loadAll().size.toString()
        val nilaiMonitoring = daoSession.tPosDao.loadAll().size.toString()
        val nilaiPemakaian = daoSession.tPemakaianDao.loadAll().size.toString()
        val nilaiPermintaan = daoSession.tMonitoringPermintaanDao.queryBuilder()
            .where(TMonitoringPermintaanDao.Properties.KodePengeluaran.eq(1))
            .list().size.toString()
        val nilaiPengiriman = daoSession.tPosDao.loadAll().size.toString()
        val nilaiPengujian = daoSession.tPengujianDao.loadAll().size.toString()
        val nilaiPenerimaanUp3 =  daoSession.tPosDao.queryBuilder()
            .where(TPosDao.Properties.KodeStatusDoMims.eq("102")).list().size.toString()

        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.popup_loading)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)
        dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown

        val session = SessionManager(requireContext())

        var mAndroidId = Helper.getAndroidId(requireActivity())
        var mAppVersion = Helper.getVersionApp(requireActivity())
        var mDeviceData = Helper.deviceData
        var mIpAddress = Helper.getIPAddress(true)
        var androidVersion = Build.VERSION.SDK_INT
        var dateTimeUtc = DateTimeUtils.currentTimeMillis()
        var username = SharedPrefsUtils.getStringPreference(requireActivity(),"username","14.Hexing_Electric")!!
        var mPassword = SharedPrefsUtils.getStringPreference(requireActivity(),"password","12345")!!

        binding.txtdash1.text = "John Doe"
        binding.btnMonitoring.visibility = View.GONE
        binding.btnPengujian.visibility = View.GONE
        binding.btnPengiriman.visibility = View.GONE
        binding.btnDataAttr.visibility = View.GONE
        binding.btnTracking.visibility = View.GONE
        binding.btnPenerimaan.visibility = View.GONE
        binding.btnPemakaian.visibility = View.GONE
        binding.btnPenerimaanUlp.visibility = View.GONE
        binding.btnMonitoringPermintaan.visibility = View.GONE

        var listPrivilege = daoSession.tPrivilegeDao.queryBuilder().list()

        for (i in listPrivilege){
            if (i.methodId == "is_monitoring" && i.isActive == "1"){
                binding.btnMonitoring.visibility = View.VISIBLE
            }

            if (i.methodId == "is_pengujian" && i.isActive == "1"){
                binding.btnPengujian.visibility = View.VISIBLE
            }

            if (i.methodId == "is_pengiriman" && i.isActive == "1"){
                binding.btnPengiriman.visibility = View.VISIBLE
            }

            if (i.methodId == "is_data_atribut" && i.isActive == "1"){
                binding.btnDataAttr.visibility = View.VISIBLE
            }

            if (i.methodId == "is_tracking" && i.isActive == "1"){
                binding.btnTracking.visibility = View.VISIBLE
            }

            if (i.methodId == "is_penerimaan" && i.isActive == "1"){
                binding.btnPenerimaan.visibility = View.VISIBLE
            }

            if (i.methodId == "is_pemakaian" && i.isActive == "1"){
                binding.btnPemakaian.visibility = View.VISIBLE
            }

            if (i.methodId == "is_penerimaan_ulp" && i.isActive == "1"){
                binding.btnPenerimaanUlp.visibility = View.VISIBLE
            }

            if (i.methodId == "is_monitoring_permintaan" && i.isActive == "1"){
                binding.btnMonitoringPermintaan.visibility = View.VISIBLE
            }

            binding.txtJumlahAttribut.text = nilaiMaterial
            binding.txtJumlahMonitoring.text = nilaiMonitoring
            binding.txtJumlahPemakaian.text = nilaiPemakaian
            binding.txtJumlahMonitoringPermintaan.text = nilaiPermintaan
            binding.txtJumlahPenerimaanUlp.text = nilaiPenerimaanUlp
            binding.txtJumlahPengiriman.text = nilaiPengiriman
            binding.txtJumlahPengujian.text = nilaiPengujian
            binding.txtJumlahTracking.text = "4" //gak tahu nilainya darimana
            binding.txtJumlahPenerimaanUp3.text = nilaiPenerimaanUp3

        }

        binding.btnMonitoringPermintaan.setOnClickListener {
            startActivity(Intent(requireActivity(), MonitoringPermintaanActivity::class.java))
        }

        binding.btnPemakaian.setOnClickListener {
            startActivity(Intent(requireActivity(), PemakaianActivity::class.java))
        }

        binding.btnPenerimaanUlp.setOnClickListener { startActivity(Intent(requireActivity(), PenerimaanUlpActivity::class.java)) }

        binding.btnSync.setOnClickListener {
            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.popup_validation);
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.window!!.attributes.windowAnimations = R.style.DialogUpDown;
            val btnYa = dialog.findViewById(R.id.btn_ya) as AppCompatButton
            val btnTidak = dialog.findViewById(R.id.btn_tidak) as AppCompatButton
            val message = dialog.findViewById(R.id.message) as TextView
            val textMessage = dialog.findViewById(R.id.txt_message) as TextView

            message.text = "Yakin untuk melakukan synchronize data?"
            textMessage.text = "Klik ya untuk melakukan synchronize data"

            btnTidak.setOnClickListener {
                dialog.dismiss();
            }

            btnYa.setOnClickListener {
                btnSync(requireActivity(),
                    daoSession,username, mPassword,"",
                    mAndroidId,mAppVersion,mDeviceData,mIpAddress,
                    androidVersion,dateTimeUtc,session)
                dialog.dismiss()
            }

            dialog.show();
        }

        binding.btnMonitoring.setOnClickListener {
            startActivity(Intent(requireActivity(), MonitoringActivity::class.java))
        }

        binding.btnDataAttr.setOnClickListener {
            startActivity(Intent(requireActivity(), DataAtributMaterialActivity::class.java))
        }

        binding.btnPengujian.setOnClickListener{
            startActivity(Intent(requireActivity(), PengujianActivity::class.java))
        }

        binding.btnPenerimaan.setOnClickListener {
            val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            val btnPemeriksaan = view.findViewById<CardView>(R.id.cv_pemeriksaan)
            val btnPenerimaan = view.findViewById<CardView>(R.id.cv_penerimaan)

            btnPenerimaan.setOnClickListener {
                startActivity(Intent(requireActivity(), PenerimaanActivity::class.java))
            }

            btnPemeriksaan.setOnClickListener {
                startActivity(Intent(requireActivity(), PemeriksaanActivity::class.java))
            }

            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }

        binding.btnTracking.setOnClickListener {
            startActivity(Intent(requireActivity(), TrackingHistoryActivity::class.java))
        }

        binding.btnPengiriman.setOnClickListener {
            startActivity(Intent(requireActivity(), PengirimanActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(requireActivity(), RegistrasiSnMaterialActivity::class.java))
        }

        binding.btnApproval.setOnClickListener {
            startActivity(Intent(requireActivity(), ApprovalActivity::class.java))
        }
    }

    private fun btnSync(context: Context, daoSession: DaoSession, username: String, password: String, device_token: String,
                        mAndroidId: String, mAppVersion: String, mDeviceData: String, mIpAddress: String,
                        androidVersion: Int, dateTimeUtc: Long,session: SessionManager) {
        dialog.show()
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
                        dialog.dismiss()
                        val loginResult = response.body()
                        inserToDbLocal(daoSession, loginResult!!)
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()

                    }catch (e: Exception){
                        dialog.dismiss()
                        e.printStackTrace()
                    }
                }else {
                    dialog.dismiss()
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
        daoSession.tPemakaianDao.deleteAll()
        daoSession.tPemakaianDetailDao.deleteAll()
        daoSession.tListSnMaterialPenerimaanUlpDao.deleteAll()
        daoSession.tListSnMaterialPemakaianUlpDao.deleteAll()
        daoSession.tPhotoDao.deleteAll()


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
                        item.qtyRusak = model?.qtyRusak.toString()
                        item.qtyTdkLolos = model?.qtyTdkLolos.toString()
                        item.tanggalUji = model?.tglUji.toString()
                        item.tanggalUsulUji = model?.tanggalUsulUji
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
                        item.idLokasi = model?.id
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