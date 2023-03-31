package dev.iconpln.mims.ui.monitoring_permintaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMonitoringPermintaan
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TTransMonitoringPermintaan
import dev.iconpln.mims.data.local.database.TTransMonitoringPermintaanDetailDao
import dev.iconpln.mims.databinding.ItemDataMonitoringPermintaanBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPurchaseBinding

class MonitoringPermintaanAdapter(val lisModels: MutableList<TTransMonitoringPermintaan>, var listener: OnAdapterListener, var daoSession: DaoSession)
    : RecyclerView.Adapter<MonitoringPermintaanAdapter.ViewHolder>() {

    fun setMpList(po: List<TTransMonitoringPermintaan>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonitoringPermintaanAdapter.ViewHolder {
        val binding = ItemDataMonitoringPermintaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonitoringPermintaanAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataMonitoringPermintaanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(mp : TTransMonitoringPermintaan){
            with(binding){
                val checkDetail = daoSession.tTransMonitoringPermintaanDetailDao.queryBuilder()
                    .where(TTransMonitoringPermintaanDetailDao.Properties.NoTransaksi.eq(mp.noTransaksi))
                    .where(TTransMonitoringPermintaanDetailDao.Properties.IsDone.eq(0)).list()

                btnDetail.setOnClickListener { listener.onClick(mp) }
                txtGudangAsal.text = mp.storLocAsalName
                txtGudangTujuan.text = mp.storLocTujuanName
                txtNoPermintaan.text = mp.noPermintaan
                val adjustTglPermintaan = mp.tanggalPermintaan.take(10)
                txtTglPermintaan.text = adjustTglPermintaan
                when(mp.kodePengeluaran){
                    "1" -> txtStatusPengeluaran.text = "Permintaan"
                    "2" -> txtStatusPengeluaran.text = "Pengeluaran"
                    "0" -> txtStatusPengeluaran.text = "All"
                    else -> txtStatusPengeluaran.text = "-"
                }

                if (checkDetail.isNullOrEmpty()){
                    btnDetail.setImageResource(R.drawable.ic_src_doc_selesai)
                }

            }
        }
    }

    interface OnAdapterListener{
        fun onClick(mp: TTransMonitoringPermintaan)
    }
}