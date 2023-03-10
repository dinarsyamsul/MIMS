package dev.iconpln.mims.ui.ulp.penerimaan.input_penerimaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.TTransPenerimaanDetailUlp
import dev.iconpln.mims.databinding.ItemDetailPemeriksaanUlpBinding
import dev.iconpln.mims.databinding.ItemDetailPenerimaanUlpBinding

class DetailPenerimaanUlpAdapter(val lisModels: MutableList<TTransPenerimaanDetailUlp>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<DetailPenerimaanUlpAdapter.ViewHolder>() {

    fun setPenerimaanList(mat: List<TTransPenerimaanDetailUlp>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDetailPenerimaanUlpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDetailPenerimaanUlpBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pengujian : TTransPenerimaanDetailUlp){
            with(binding){
                txtKuantitas.text = pengujian.qtyPenerimaan.toString()
                txtKuantitasPeriksa.text = pengujian.qtyPemeriksaan.toString()
                txtNoMaterial.text = pengujian.noMaterial

                ivDelivery.setOnClickListener { listener.onClick(pengujian) }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: TTransPenerimaanDetailUlp)
    }
}