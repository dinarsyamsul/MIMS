package dev.iconpln.mims.ui.monitoring

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.databinding.ItemDataMonitoringPurchaseBinding

class MonitoringAdapter(val lisModels: MutableList<TPos>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<MonitoringAdapter.ViewHolder>() {

    fun setPoList(po: List<TPos>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonitoringAdapter.ViewHolder {
        val binding = ItemDataMonitoringPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonitoringAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataMonitoringPurchaseBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : TPos){
            with(binding){
                txtNoDoNew.text = po.noDoSmar
                txtNoPo.text = po.poMpNo
                txtNoTlsk.text = po.tlskNo
                txtKuantitas.text = po.total
                txtDeliveryDate.text = po.createdDate
            }

            itemView.setOnClickListener { listener.onClick(po) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPos)
    }
}