package dev.iconpln.mims.ui.monitoring_complaint.detail_monitoring_complaint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TMonitoringComplaintDetail
import dev.iconpln.mims.databinding.ItemListMonitoringKomplainBinding

class MonitoringComplaintDetailAdapter(val lisModels: MutableList<TMonitoringComplaintDetail>,
                                       var listenerNormal: MonitoringComplaintDetailAdapter.OnAdapterListenerNormal,
                                       var listenerCacat: MonitoringComplaintDetailAdapter.OnAdapterListenerCacat,
                                       var listenerAlasan: MonitoringComplaintDetailAdapter.OnAdapterAlasanKomplain,
                                       var daoSession: DaoSession, var subrole: Int, var status: String)
    : RecyclerView.Adapter<MonitoringComplaintDetailAdapter.ViewHolder>() {

    fun setComplaint(complaint: List<TMonitoringComplaintDetail>){
        lisModels.clear()
        lisModels.addAll(complaint)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonitoringComplaintDetailAdapter.ViewHolder {
        val binding = ItemListMonitoringKomplainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonitoringComplaintDetailAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemListMonitoringKomplainBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(complaint : TMonitoringComplaintDetail){
            with(binding){
                txtDeliveryOrder.text = complaint.noSerial
                txtNoKomplain.text = complaint.noKomplain
                txtStatusKomplain.text = complaint.status
                txtNoPackaging.text = complaint.noPackaging
                txtNoMatSap.text = complaint.noMatSap
                txtTanggalPengajuan.text = complaint.tanggalPengajuan
                txtTanggalSelesai.text = if (complaint.tanggalSelesai.isNullOrEmpty()) "-" else complaint.tanggalSelesai

                txtAlasanKomplain.setOnClickListener {
                    listenerAlasan.onClick(complaint)
                }

                if (status == "ACTIVE"){
                    cbSesuai.isEnabled = false
                    cbTidakSesuai.isEnabled = false
                }

                if (subrole == 3){
                    cbSesuai.visibility = View.GONE
                    cbTidakSesuai.visibility = View.GONE
                }

                if (complaint.isChecked == 1 && complaint.statusPeriksa == "SESUAI"){
                    cbSesuai.isChecked = true
                    cbTidakSesuai.isChecked = false
                }else if (complaint.isChecked == 1 && complaint.statusPeriksa == "TIDAK SESUAI"){
                    cbTidakSesuai.isChecked = true
                    cbSesuai.isChecked = false
                }else {
                    cbSesuai.isChecked = false
                    cbTidakSesuai.isChecked = false
                }

                cbTidakSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbSesuai.isEnabled = !isChecked
                    listenerCacat.onClick(isChecked)

                    if (isChecked){
                        complaint.statusPeriksa = "TIDAK SESUAI"
                        complaint.isChecked = 1
                        daoSession.tMonitoringComplaintDetailDao.update(complaint)
                    }else{
                        complaint.statusPeriksa = ""
                        complaint.isChecked = 0
                        daoSession.tMonitoringComplaintDetailDao.update(complaint)
                    }
                }

                cbSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbTidakSesuai.isEnabled = !isChecked
                    listenerNormal.onClick(isChecked)

                    if (isChecked){
                        complaint.statusPeriksa = "SESUAI"
                        complaint.isChecked = 1
                        daoSession.tMonitoringComplaintDetailDao.update(complaint)
                    }else{
                        complaint.statusPeriksa = ""
                        complaint.isChecked = 0
                        daoSession.tMonitoringComplaintDetailDao.update(complaint)
                    }

                }
            }
        }
    }

    interface OnAdapterListenerNormal{
        fun onClick(po: Boolean)
    }

    interface  OnAdapterListenerCacat{
        fun onClick(po: Boolean)
    }

    interface OnAdapterAlasanKomplain{
        fun onClick(po: TMonitoringComplaintDetail)
    }
}