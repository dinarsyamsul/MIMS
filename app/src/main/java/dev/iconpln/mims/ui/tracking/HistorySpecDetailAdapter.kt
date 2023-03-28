package dev.iconpln.mims.ui.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.DataModelHistory
import dev.iconpln.mims.data.remote.response.HistorisItem
import dev.iconpln.mims.databinding.ItemHistoryDetailBinding
import dev.iconpln.mims.databinding.ItemTrackingHistoryBinding
import dev.iconpln.mims.databinding.ItemTrackingMaterialBinding
import dev.iconpln.mims.databinding.ItemTrackingMaterialHistoryBinding

class HistorySpecDetailAdapter(val lisModels: MutableList<DataModelHistory>)
    : RecyclerView.Adapter<HistorySpecDetailAdapter.ViewHolder>() {

    fun setData(po: List<DataModelHistory>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistorySpecDetailAdapter.ViewHolder {
        val binding = ItemHistoryDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorySpecDetailAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemHistoryDetailBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : DataModelHistory){
            with(binding){
                txtKey.text = data.key
                txtValue.text = data.value
            }
        }
    }
}