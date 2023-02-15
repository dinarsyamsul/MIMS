package dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.databinding.ItemPackagingBinding
import dev.iconpln.mims.databinding.ItemPackagingPemeriksaanBinding
import dev.iconpln.mims.databinding.ItemSnMaterialBinding

class DetailPemeriksaanAdapter(val lisModels: MutableList<TPemeriksaanDetail>,
                               var listener: OnAdapterListener,var daoSession: DaoSession)
    : RecyclerView.Adapter<DetailPemeriksaanAdapter.ViewHolder>() {

    fun setPedList(po: List<TPemeriksaanDetail>){
        lisModels.clear()
        lisModels.addAll(po)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSnMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemSnMaterialBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : TPemeriksaanDetail){

            with(binding){
                txtSnMaterial.text = "${po.sn}"
                txtKategori.text = po.kategori
                txtVendor.text = "-"

//                cbSesuai.isChecked = po.isDone == 1

                if (po.isChecked == 1 && po.statusSn == "SESUAI"){
                    cbSesuai.isChecked = true
                }else if (po.isChecked == 1 && po.statusSn == "TIDAK SESUAI"){
                    cbTidakSesuai.isChecked = true
                }else {
                    cbSesuai.isChecked = false
                    cbTidakSesuai.isChecked = false
                }

                cbTidakSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbSesuai.isEnabled = !isChecked
                    po.statusSn = "TIDAK SESUAI"
                    po.isChecked = 1
                    daoSession.tPemeriksaanDetailDao.update(po)
                }

                cbSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbTidakSesuai.isEnabled = !isChecked
                    po.statusSn = "SESUAI"
                    po.isChecked = 1
                    daoSession.tPemeriksaanDetailDao.update(po)
                }
            }
            itemView.setOnClickListener { listener.onClick(po) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPemeriksaanDetail)
    }
}