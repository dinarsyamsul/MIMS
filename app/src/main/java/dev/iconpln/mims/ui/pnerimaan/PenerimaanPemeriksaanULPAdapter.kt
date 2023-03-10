package dev.iconpln.mims.ui.pnerimaan

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.data.remote.PenerimaanPEmeriksaanUlpMandiriData
import dev.iconpln.mims.databinding.ItemDataPenerimaanPemeriksaanulpBinding
import dev.iconpln.mims.databinding.ItemPackagingBinding
import dev.iconpln.mims.databinding.ItemPackagingPemeriksaanBinding
import dev.iconpln.mims.databinding.ItemSnMaterialBinding

class PenerimaanPemeriksaanULPAdapter(val lisModels: MutableList<PenerimaanPEmeriksaanUlpMandiriData>,
                                      var listener: OnAdapterListener, var daoSession: DaoSession)
    : RecyclerView.Adapter<PenerimaanPemeriksaanULPAdapter.ViewHolder>() {

    fun setData(po: List<PenerimaanPEmeriksaanUlpMandiriData>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataPenerimaanPemeriksaanulpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPenerimaanPemeriksaanulpBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tpd : PenerimaanPEmeriksaanUlpMandiriData){

            with(binding){

            }
            itemView.setOnClickListener { listener.onClick(tpd) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: PenerimaanPEmeriksaanUlpMandiriData)
    }
}