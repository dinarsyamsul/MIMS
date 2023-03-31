package dev.iconpln.mims.ui.pemeriksaan.pemeriksaan_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.databinding.ItemSnPemeriksaanBinding

class DetailPemeriksaanAdapter(val lisModels: MutableList<TPemeriksaanDetail>,
                               var listenerNormal: OnAdapterListenerNormal,var listenerCacat: OnAdapterListenerCacat ,var daoSession: DaoSession)
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
        val binding = ItemSnPemeriksaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemSnPemeriksaanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : TPemeriksaanDetail){

            with(binding){
                txtSnMaterial.text = "${po.sn}"
                txtKategori.text = po.kategori
                txtVendor.text = "-"
                txtNoPackaging.text = po.noPackaging

//                cbSesuai.isChecked = po.isDone == 1

                if (po.isChecked == 1 && po.statusPemeriksaan == "NORMAL"){
                    cbSesuai.isChecked = true
                    cbTidakSesuai.isChecked = false
                }else if (po.isChecked == 1 && po.statusPemeriksaan == "CACAT"){
                    cbTidakSesuai.isChecked = true
                    cbSesuai.isChecked = false
                }else {
                    cbSesuai.isChecked = false
                    cbTidakSesuai.isChecked = false
                }

                cbTidakSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbSesuai.isEnabled = !isChecked
                    listenerCacat.onClick(isChecked)

                    if (isChecked){
                        po.statusPemeriksaan = "CACAT"
                        po.isChecked = 1
                        daoSession.tPemeriksaanDetailDao.update(po)
                    }else{
                        po.statusPemeriksaan = ""
                        po.isChecked = 0
                        daoSession.tPemeriksaanDetailDao.update(po)
                    }
                }

                cbSesuai.setOnCheckedChangeListener { buttonView, isChecked ->
                    cbTidakSesuai.isEnabled = !isChecked
                    listenerNormal.onClick(isChecked)

                    if (isChecked){
                        po.statusPemeriksaan = "NORMAL"
                        po.isChecked = 1
                        daoSession.tPemeriksaanDetailDao.update(po)
                    }else{
                        po.statusPemeriksaan = ""
                        po.isChecked = 0
                        daoSession.tPemeriksaanDetailDao.update(po)
                    }

                }
            }
        }
    }

    interface OnAdapterListenerNormal{
        fun onClick(po: Boolean)
    }

    interface  OnAdapterListenerCacat{
        fun onClick(po: Boolean)
    }
}