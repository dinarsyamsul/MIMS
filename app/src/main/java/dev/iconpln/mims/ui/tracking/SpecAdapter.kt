package dev.iconpln.mims.ui.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DatasItem
import dev.iconpln.mims.databinding.ItemTrackingMaterialBinding

class SpecAdapter(val lisModels: MutableList<DatasItem>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<SpecAdapter.ViewHolder>() {

    fun setData(po: List<DatasItem>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecAdapter.ViewHolder {
        val binding = ItemTrackingMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position],position)
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemTrackingMaterialBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : DatasItem,position: Int){
            with(binding){
                lblSpec.text=data.propertyName
                txtSpec.text=data.propertyValue
            }
            itemView.setOnClickListener { listener.onClick(data) }
        }
    }

    interface OnAdapterListener{
        fun onClick(data: DatasItem)
    }
}