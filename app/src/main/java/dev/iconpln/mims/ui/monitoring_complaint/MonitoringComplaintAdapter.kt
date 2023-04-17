package dev.iconpln.mims.ui.monitoring_complaint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.databinding.ItemDataMonitoringKomplainBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPermintaanBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPurchaseBinding

class MonitoringComplaintAdapter(val lisModels: MutableList<TMonitoringComplaint>,
                                 var listener: OnAdapterListener, val daoSession: DaoSession, var subrole: Int)
    : RecyclerView.Adapter<MonitoringComplaintAdapter.ViewHolder>() {

    fun setComplaint(complaint: List<TMonitoringComplaint>){
        lisModels.clear()
        lisModels.addAll(complaint)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonitoringComplaintAdapter.ViewHolder {
        val binding = ItemDataMonitoringKomplainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonitoringComplaintAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataMonitoringKomplainBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(complaint : TMonitoringComplaint){
            with(binding){
                txtDeliveryOrder.text = complaint.noDoSmar
                txtNoPo.text = if (complaint.poSapNo.isNullOrEmpty()) "-" else complaint.poSapNo
                txtStatusKomplain.text = complaint.status
                txtUnitTujuan.text = complaint.plantName
                tvTanggalPo.text = "Tgl PO "+complaint.tanggalPO

                if (subrole != 3){
                    val checkComplaintDetail = daoSession.tMonitoringComplaintDetailDao.queryBuilder()
                        .where(TMonitoringComplaintDetailDao.Properties.NoKomplain.eq(complaint.noKomplain))
                        .where(TMonitoringComplaintDetailDao.Properties.Status.eq("SEDANG KOMPLAIN")).list().size > 0

                    if (checkComplaintDetail){
                        btnMontioring.setImageResource(R.drawable.ic_input_doc_active)
                    }else{
                        btnMontioring.setImageResource(R.drawable.ic_input_doc_done)
                    }
                }

                btnMontioring.setOnClickListener { listener.onClick(complaint) }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(mp: TMonitoringComplaint)
    }
}