package dev.iconpln.mims.ui.home

import android.app.AlertDialog
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
import dev.iconpln.mims.ui.pemeriksaan.PemeriksaanActivity
import dev.iconpln.mims.ui.pengiriman.PengirimanActivity
import dev.iconpln.mims.ui.pnerimaan.PenerimaanActivity
import dev.iconpln.mims.ui.arttribute_material.DataAtributMaterialActivity
import dev.iconpln.mims.ui.pengujian.PengujianActivity
import dev.iconpln.mims.ui.tracking.TrackingHistoryActivity
import dev.iconpln.mims.utils.Helper
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTimeUtils
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = SessionManager(requireContext())
        val daoSession = (requireActivity().application as MyApplication).daoSession!!

        var mAndroidId = Helper.getAndroidId(requireActivity())
        var mAppVersion = Helper.getVersionApp(requireActivity())
        var mDeviceData = Helper.deviceData
        var mIpAddress = Helper.getIPAddress(true)
        var androidVersion = Build.VERSION.SDK_INT
        var dateTimeUtc = DateTimeUtils.currentTimeMillis()
        var username = SharedPrefsUtils.getStringPreference(requireActivity(),"username","14.Hexing_Electric")!!
        var mPassword = SharedPrefsUtils.getStringPreference(requireActivity(),"password","12345")!!

        binding.txtdash1.text = "John Doe"

        var listPrivilege = daoSession.tPrivilegeDao.queryBuilder().list()

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
                Toast.makeText(requireActivity(),
                    android.R.string.yes, Toast.LENGTH_SHORT).show()
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
    }

    private fun btnSync(context: Context, daoSession: DaoSession, username: String, password: String, device_token: String,
                        mAndroidId: String, mAppVersion: String, mDeviceData: String, mIpAddress: String,
                        androidVersion: Int, dateTimeUtc: Long,session: SessionManager) {
        binding.pgBar.visibility = View.VISIBLE
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
                        binding.pgBar.visibility = View.GONE
                        val loginResult = response.body()
                        inserToDbLocal(daoSession, loginResult!!)
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }else {
                    binding.pgBar.visibility = View.GONE
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
                        item.noPackaging = model?.noPackaging

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