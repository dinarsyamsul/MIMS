package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan

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

class DetailPenerimaanAdapter(val lisModels: MutableList<TPosDetailPenerimaan>,
                              var listener: OnAdapterListener, var daoSession: DaoSession)
    : RecyclerView.Adapter<DetailPenerimaanAdapter.ViewHolder>() {

    fun setData(po: List<TPosDetailPenerimaan>){
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
        fun bind(tpd : TPosDetailPenerimaan){

            with(binding){
                txtSnMaterial.text = "${tpd.serialNumber}"
                txtKategori.text = tpd.namaKategoriMaterial
                txtVendor.text = "-"

//                cbSesuai.isChecked = po.isDone == 1

                if (tpd.isChecked == 1 && tpd.status == "SESUAI"){
                    cbSesuai.isChecked = true
                }else if (tpd.isChecked == 1 && tpd.status == "TIDAK SESUAI"){
                    cbTidakSesuai.isChecked = true
                }else {
                    cbSesuai.isChecked = false
                    cbTidakSesuai.isChecked = false
                }

                cbTidakSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbSesuai.isEnabled = !isChecked
                    if (isChecked){
                        tpd.status = "TIDAK SESUAI"
                        tpd.isChecked = 1
                        daoSession.tPosDetailPenerimaanDao.update(tpd)
                    }else{
                        tpd.status = ""
                        tpd.isChecked = 0
                        daoSession.tPosDetailPenerimaanDao.update(tpd)
                    }
                }

                cbSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbTidakSesuai.isEnabled = !isChecked
                    if (isChecked){
                        tpd.status = "SESUAI"
                        tpd.isChecked = 1
                        daoSession.tPosDetailPenerimaanDao.update(tpd)
                    }else{
                        tpd.status = ""
                        tpd.isChecked = 0
                        daoSession.tPosDetailPenerimaanDao.update(tpd)
                    }

                }
            }
            itemView.setOnClickListener { listener.onClick(tpd) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPosDetailPenerimaan)
    }
}