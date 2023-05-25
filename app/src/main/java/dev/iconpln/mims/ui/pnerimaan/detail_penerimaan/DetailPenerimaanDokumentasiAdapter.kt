package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.databinding.ItemDokumentasiBinding
import dev.iconpln.mims.databinding.ItemPackagingBinding
import dev.iconpln.mims.databinding.ItemPackagingPemeriksaanBinding
import dev.iconpln.mims.databinding.ItemSnMaterialBinding

class DetailPenerimaanDokumentasiAdapter(val lisModels: MutableList<String>)
    : RecyclerView.Adapter<DetailPenerimaanDokumentasiAdapter.ViewHolder>() {

    fun setData(po: List<String>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDokumentasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDokumentasiBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tpd : String){

            with(binding) {
                Glide.with(itemView)
                    .load("http://10.14.152.192:30029/resource/file/getFile/$tpd")
                    .placeholder(R.drawable.ic_not_found)
                    .into(binding.ivDokumentasi)
            }
        }
    }
}