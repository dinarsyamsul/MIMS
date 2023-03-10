package dev.iconpln.mims.ui.pengujian

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPengujian
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
                txtSiapTotal.text = "${pengujian.qtyLolos}/${pengujian.qtyMaterial}"
                txtTanggalUji.text = pengujian.tanggalUji
                txtStatus.text = pengujian.statusUji.trim()
            }

            if (pengujian.statusUji == "LOLOS") {
                binding.txtStatus.setBackgroundColor(Color.parseColor("#4600C637"))
                binding.txtStatus.setTextColor(Color.parseColor("#00C637"))
            }else if (pengujian.statusUji == "TIDAK LOLOS"){
                binding.txtStatus.setBackgroundColor(Color.parseColor("#3EB80F0A"))
                binding.txtStatus.setTextColor(Color.parseColor("#B80F0A"))
            }else if (pengujian.statusUji == "BELUM UJI"){
                binding.txtStatus.setBackgroundColor(Color.parseColor("#41F8951D"))
                binding.txtStatus.setTextColor(Color.parseColor("#045A71"))
            }else if (pengujian.statusUji == "SEDANG UJI"){
                binding.txtStatus.setBackgroundColor(Color.parseColor("#52F8951D"))
                binding.txtStatus.setTextColor(Color.parseColor("#F8951D"))
            }else {
                binding.txtStatus.setBackgroundColor(Color.parseColor("#E8E8E8"))
            }

            itemView.setOnClickListener { listener.onClick(pengujian) }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: TPengujian)
    }
}