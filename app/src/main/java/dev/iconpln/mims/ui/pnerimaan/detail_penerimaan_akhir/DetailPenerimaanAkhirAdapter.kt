package dev.iconpln.mims.ui.pnerimaan.detail_penerimaan_akhir

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaan
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaanAkhir
import dev.iconpln.mims.databinding.ItemSnMaterialBinding

class DetailPenerimaanAkhirAdapter(val lisModels: MutableList<TPosDetailPenerimaanAkhir>,
                              var listener: OnAdapterListener, var daoSession: DaoSession)
    : RecyclerView.Adapter<DetailPenerimaanAkhirAdapter.ViewHolder>() {

    fun setData(po: List<TPosDetailPenerimaanAkhir>){
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
        fun bind(tpd : TPosDetailPenerimaanAkhir){

            with(binding){
                txtSnMaterial.text = "${tpd.serialNumber}"
                txtKategori.text = tpd.namaKategoriMaterial
                txtVendor.text = tpd.storLoc

                if (tpd.isReceived == true){
                    cbSesuai.isChecked = true
                    cbTidakSesuai.isChecked = false
                }else if (tpd.isComplaint == true || tpd.isRejected == true){
                    cbSesuai.isChecked = false
                    cbTidakSesuai.isChecked = true
                }else{
                    cbSesuai.isChecked = false
                    cbTidakSesuai.isChecked = false
                }

                cbSesuai.isEnabled = false
                cbTidakSesuai.isEnabled = false
            }
            itemView.setOnClickListener { listener.onClick(tpd) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPosDetailPenerimaanAkhir)
    }
}