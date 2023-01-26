package dev.iconpln.mims.ui.role.pabrikan.pengujian

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TMaterial
import dev.iconpln.mims.data.local.database.TPengujian
import dev.iconpln.mims.databinding.ItemDataMaterialPabrikanBinding
import dev.iconpln.mims.databinding.ItemPengujianBinding

class PengujianAdapter(val lisModels: MutableList<TPengujian>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PengujianAdapter.ViewHolder>() {

    fun setPengujianList(mat: List<TPengujian>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPengujianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemPengujianBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pengujian : TPengujian){
            with(binding){
                txtNoPengujian.text = pengujian.noPengujian
                txtKategori.text = pengujian.namaKategori
                txtSatuan.text = pengujian.unit
                txtSiapTotal.text = "${pengujian.qtySiap}/${pengujian.qtyMaterial}"
                txtTanggalUji.text = pengujian.tanggalUji
                txtStatus.text = pengujian.statusUji
            }

            itemView.setOnClickListener { listener.onClick(pengujian) }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: TPengujian)
    }
}