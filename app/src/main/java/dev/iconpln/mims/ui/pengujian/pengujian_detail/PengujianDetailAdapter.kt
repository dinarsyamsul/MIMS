package dev.iconpln.mims.ui.pengujian.pengujian_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPengujianDetails
import dev.iconpln.mims.databinding.ItemListDetailPengujianBinding

class PengujianDetailAdapter(val lisModels: MutableList<TPengujianDetails>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PengujianDetailAdapter.ViewHolder>() {

    fun setPengujianList(pengujian: List<TPengujianDetails>){
        lisModels.clear()
        lisModels.addAll(pengujian)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemListDetailPengujianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemListDetailPengujianBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pengujian : TPengujianDetails){
            with(binding){
                txtSerialNumber.text = pengujian.serialNumber
                status.text = pengujian.statusUji
            }

            itemView.setOnClickListener { listener.onClick(pengujian) }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: TPengujianDetails)
    }
}