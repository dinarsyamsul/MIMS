package dev.iconpln.mims.ui.role.up3.pnerimaan.detail_penerimaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosDetail
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ItemDataDetailPurchaseOrderBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPurchaseBinding
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding
import dev.iconpln.mims.databinding.ItemPackagingBinding

class DetailPenerimaanAdapter(val lisModels: MutableList<TPosDetailPenerimaan>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<DetailPenerimaanAdapter.ViewHolder>() {

    fun setPoList(po: List<TPosDetailPenerimaan>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailPenerimaanAdapter.ViewHolder {
        val binding = ItemPackagingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailPenerimaanAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemPackagingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : TPosDetailPenerimaan){
            with(binding){
                txtNoPackaging.text = po.noPackaging
                if(po.isDone == "1"){
                    checkReceived.isChecked = true
                    checkReceived.isEnabled = false
                }
            }

            itemView.setOnClickListener { listener.onClick(po) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPosDetailPenerimaan)
    }
}