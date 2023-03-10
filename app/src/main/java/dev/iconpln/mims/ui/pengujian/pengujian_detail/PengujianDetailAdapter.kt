package dev.iconpln.mims.ui.pengujian.pengujian_detail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPengujianDetails
import dev.iconpln.mims.databinding.ItemListDetailPengujianBinding

class PengujianDetailAdapter(val lisModels: MutableList<TPengujianDetails>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PengujianDetailAdapter.ViewHolder>() {

    fun setPengujianList(pengujian: List<TPengujianDetails>){
        lisModels.clear()
        lisModels.addAll(pengujian)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemListDetailPengujianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemListDetailPengujianBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pengujian : TPengujianDetails){
            with(binding){
                txtSerialNumber.text = pengujian.serialNumber
                status.text = pengujian.statusUji
            }

            if (pengujian.statusUji == "LOLOS") {
                binding.status.setBackgroundColor(Color.parseColor("#4600C637"))
                binding.status.setTextColor(Color.parseColor("#00C637"))
            }else if (pengujian.statusUji == "TIDAK LOLOS"){
                binding.status.setBackgroundColor(Color.parseColor("#3EB80F0A"))
                binding.status.setTextColor(Color.parseColor("#B80F0A"))
            }else  {
                binding.status.setBackgroundColor(Color.parseColor("#90DFDFDF"))
                binding.status.setTextColor(Color.parseColor("#000000"))
            }

            itemView.setOnClickListener { listener.onClick(pengujian) }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: TPengujianDetails)
    }
}