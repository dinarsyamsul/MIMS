package dev.iconpln.mims.ui.arttribute_material.detail_attribute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TMaterialDetail
import dev.iconpln.mims.databinding.ItemDataDetailMaterialPabrikanBinding

class DetailDataAttributeAdapter(val lisModels: MutableList<TMaterialDetail>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<DetailDataAttributeAdapter.ViewHolder>() {

    fun setMaterialList(mat: List<TMaterialDetail>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataDetailMaterialPabrikanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataDetailMaterialPabrikanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(mat : TMaterialDetail){
            with(binding){
                txtSerialNumber.text = if(mat.serialNumber.isNullOrEmpty()) "-" else mat.serialNumber
                txtNoMaterial.text = if(mat.nomorMaterial.isNullOrEmpty()) "-" else mat.nomorMaterial
                txtGaransi.text = if(mat.masaGaransi.isNullOrEmpty()) "-" else mat.masaGaransi
                txtKategori.text = if(mat.namaKategoriMaterial.isNullOrEmpty()) "-" else mat.namaKategoriMaterial
                txtMetrologi.text = if(mat.nomorSertMaterologi.isNullOrEmpty()) "-" else mat.nomorSertMaterologi
                txtSpesifikasi.text = if(mat.spesifikasi.isNullOrEmpty()) "-" else mat.spesifikasi
                txtNoPackaging.text = if(mat.noPackaging.isNullOrEmpty()) "-" else mat.noPackaging
                txtSpln.text = if(mat.spln.isNullOrEmpty()) "-" else mat.spln
                txtTglProduksi.text = if(mat.tglProduksi.isNullOrEmpty()) "-" else mat.tglProduksi
                txtNoBatch.text = if(mat.noProduksi.isNullOrEmpty()) "-" else mat.noProduksi
            }

            itemView.setOnClickListener { listener.onClick(mat) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TMaterialDetail)
    }
}