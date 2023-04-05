package dev.iconpln.mims.ui.arttribute_material

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TMaterial
import dev.iconpln.mims.databinding.ItemDataMaterialPabrikanBinding

class DataAttributeAdapter(val lisModels: MutableList<TMaterial>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<DataAttributeAdapter.ViewHolder>() {

    fun setMaterialList(mat: List<TMaterial>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataMaterialPabrikanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataMaterialPabrikanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(mat : TMaterial){
            with(binding){
                txtSerialNumber.text = mat.noProduksi
                txtNoMaterial.text = mat.nomorMaterial
                txtNoKategori.text = mat.namaKategoriMaterial
                txtNoTahun.text = mat.tahun
                txtTglProduksi.text = mat.tglProduksi.take(10)
            }

            itemView.setOnClickListener { listener.onClick(mat) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TMaterial)
    }
}