package dev.iconpln.mims.ui.pemakaian

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPemakaian
import dev.iconpln.mims.data.local.database.TPemakaianDetail
import dev.iconpln.mims.data.remote.PemakaianUlpData
import dev.iconpln.mims.databinding.ItemDataPemakaianUlpBinding

class PemakaianAdapter(val lisModels: MutableList<TPemakaian>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PemakaianAdapter.ViewHolder>() {

    fun setpemakaianList(mat: List<TPemakaian>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataPemakaianUlpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPemakaianUlpBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pemakaian : TPemakaian){
            with(binding){
                txtNoReservasi.text = if(pemakaian.noReservasi.isNullOrEmpty()) "-" else pemakaian.noReservasi
                txtStatusReservasi.text = if(pemakaian.statusKirimAgo.isNullOrEmpty()) "-" else pemakaian.statusSap
                txtTglReservasi.text = if(pemakaian.tanggalReservasi.isNullOrEmpty()) "-" else pemakaian.tanggalReservasi
                txtStatusKirimSap.text = if(pemakaian.statusSap.isNullOrEmpty()) "-" else pemakaian.statusSap
                txtJenisPekerjaan.text = if(pemakaian.jenisPekerjaan.isNullOrEmpty()) "-" else pemakaian.jenisPekerjaan
                txtSumberReservasi.text = if(pemakaian.sumber.isNullOrEmpty()) "-" else pemakaian.sumber

                ivDelivery.setOnClickListener { listener.onClick(pemakaian) }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(pemakaian: TPemakaian)
    }
}