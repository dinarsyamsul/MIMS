package dev.iconpln.mims.ui.monitoring_permintaan.monitoring_permintaan_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TMonitoringPermintaan
import dev.iconpln.mims.data.local.database.TMonitoringPermintaanDetail
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.databinding.ItemDataMonitoringPermintaanBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPermintaanDetailBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPurchaseBinding

class MonitoringPermintaanDetailAdapter(val lisModels: MutableList<TMonitoringPermintaanDetail>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<MonitoringPermintaanDetailAdapter.ViewHolder>() {

    fun setMpList(po: List<TMonitoringPermintaanDetail>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonitoringPermintaanDetailAdapter.ViewHolder {
        val binding = ItemDataMonitoringPermintaanDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonitoringPermintaanDetailAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataMonitoringPermintaanDetailBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(mpd : TMonitoringPermintaanDetail){
            with(binding){
                btnDetail.setOnClickListener { listener.onClick(mpd) }
                txtKuantitas.text = mpd.qtyPermintaan
                txtKuantitasScan.text = mpd.qtyScan
                txtSatuan.text = mpd.unit
                txtNoMaterial.text = mpd.nomorMaterial
                ltxtDescMaterial.text = mpd.materialDesc

            }
        }
    }

    interface OnAdapterListener{
        fun onClick(mpd: TMonitoringPermintaanDetail)
    }
}