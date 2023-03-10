package dev.iconpln.mims.ui.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.response.HistorisItem
import dev.iconpln.mims.databinding.ItemTrackingHistoryBinding
import dev.iconpln.mims.databinding.ItemTrackingMaterialBinding
import dev.iconpln.mims.databinding.ItemTrackingMaterialHistoryBinding

class HistorySpecAdapter(val lisModels: MutableList<HistorisItem>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<HistorySpecAdapter.ViewHolder>() {

    fun setData(po: List<HistorisItem>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistorySpecAdapter.ViewHolder {
        val binding = ItemTrackingMaterialHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorySpecAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemTrackingMaterialHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : HistorisItem){
            with(binding){
                if(data.statusName.isNullOrEmpty()) txtSpec.text = "-" else txtSpec.text = data.statusName
            }
            itemView.setOnClickListener { listener.onClick(data) }
        }
    }

    interface OnAdapterListener{
        fun onClick(data: HistorisItem)
    }
}