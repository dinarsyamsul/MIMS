package dev.iconpln.mims.ui.pengujian.petugas

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPengujian
import dev.iconpln.mims.data.local.database.TPetugasPengujian
import dev.iconpln.mims.databinding.ItemPengujianBinding
import dev.iconpln.mims.databinding.ItemPetugasPengujianBinding

class PetugasPengujianAdapter(val lisModels: MutableList<TPetugasPengujian>)
    : RecyclerView.Adapter<PetugasPengujianAdapter.ViewHolder>() {

    fun setPengujianList(petugas: List<TPetugasPengujian>){
        lisModels.clear()
        lisModels.addAll(petugas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPetugasPengujianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemPetugasPengujianBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(petugas : TPetugasPengujian){
            with(binding){
                txtNip.text = petugas.nip
                lblTitle.text = petugas.jabatan
                txtTitle.text = petugas.namaPetugas
            }
        }
    }
}