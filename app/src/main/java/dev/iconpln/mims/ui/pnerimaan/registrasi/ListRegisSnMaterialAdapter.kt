package dev.iconpln.mims.ui.pnerimaan.registrasi

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemRegisSn
import dev.iconpln.mims.databinding.ItemRegisSnMaterialBinding

class ListRegisSnMaterialAdapter : RecyclerView.Adapter<ListRegisSnMaterialAdapter.ListViewHolder>() {
    private val listRegisSn = ArrayList<DataItemRegisSn>()

    fun setListRegisSn(listDoa: List<DataItemRegisSn>) {
        this.listRegisSn.clear()
        this.listRegisSn.addAll(listDoa)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRegisSnMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listRegisSn.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listRegisSn[position])
    }

    inner class ListViewHolder(private val binding: ItemRegisSnMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemRegisSn) {
            with(binding) {
                txtSnMaterial.text = item.serialNumber
                txtNoMaterial.text = item.nomorMaterial
                txtTanggalRegis.text = item.tglRegistrasi
                txtTahun.text = item.tahun
                txtVendor.text = item.vendor
                txtNoBatch.text = item.noProduksi
            }
        }
    }
}