package dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.databinding.ItemPackagingBinding
import dev.iconpln.mims.databinding.ItemPackagingPemeriksaanBinding

class DetailPemeriksaanAdapter(val lisModels: MutableList<String>,
                               var listener: OnAdapterListener, var daoSession: DaoSession)
    : RecyclerView.Adapter<DetailPemeriksaanAdapter.ViewHolder>() {

    fun setPedList(po: List<String>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPackagingPemeriksaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemPackagingPemeriksaanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : String){

            with(binding){
                txtSnMaterial.text = "SN Material : $po"
                txtKategori.text = "Kategori: -"
                txtVendor.text = "Vendor: -"

                checkCacat.setOnCheckedChangeListener { buttonView, isChecked ->
                    checkNormal.isEnabled = !isChecked
                }

                checkNormal.setOnCheckedChangeListener { buttonView, isChecked ->
                    checkCacat.isEnabled = !isChecked
                }
            }
            itemView.setOnClickListener { listener.onClick(po) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: String)
    }
}