package dev.iconpln.mims.ui.pengiriman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TLokasi
import dev.iconpln.mims.data.remote.response.DatasItemLokasi
import dev.iconpln.mims.databinding.ItemHistoryBinding

class HistoryAdapter(val lisModels: MutableList<DatasItemLokasi>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    fun setData(po: List<DatasItemLokasi>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position],position)
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : DatasItemLokasi,position: Int){
            with(binding){
                txtLokasi.text=data.ket
                txtDate.text=data.updatedDate

                if(position==0){
                    viewStatusTop.visibility= View.GONE
                }
            }
            binding.btnDelete.setOnClickListener { listener.onClick(data) }
        }
    }

    interface OnAdapterListener{
        fun onClick(data: DatasItemLokasi)
    }
}