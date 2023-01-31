package dev.iconpln.mims.ui.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.remote.response.HistorisItem
import dev.iconpln.mims.databinding.ItemTrackingHistoryBinding
import dev.iconpln.mims.databinding.ItemTrackingMaterialBinding

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
        val binding = ItemTrackingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorySpecAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemTrackingHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : HistorisItem){
            with(binding){
                txtSpec.text=data.statusName
                when (data.statusName) {
                    "UPLOAD DATA AWAL PABRIKAN" -> {
                        btnStatus.setImageResource(R.drawable.icon_atribut);
                    }
                    "PENGAJUAN UST" -> {
                        btnStatus.setImageResource(R.drawable.icon_pengujian);

                    }
                    "HASIL UST TIDAK LOLOS" -> {
                        btnStatus.setImageResource(R.drawable.icon_pengujian);

                    }
                    else -> {
                        btnStatus.setImageResource(R.drawable.icon_penerimaan);
                    }
                }
            }
            itemView.setOnClickListener { listener.onClick(data) }
        }
    }

    interface OnAdapterListener{
        fun onClick(data: HistorisItem)
    }
}