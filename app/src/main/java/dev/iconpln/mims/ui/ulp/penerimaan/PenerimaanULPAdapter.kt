package dev.iconpln.mims.ui.ulp.penerimaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.TTransPenerimaanUlp
import dev.iconpln.mims.databinding.ItemDataPemeriksaanulpBinding

class PenerimaanULPAdapter(val lisModels: MutableList<TTransPenerimaanUlp>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PenerimaanULPAdapter.ViewHolder>() {

    fun setPenerimaanList(mat: List<TTransPenerimaanUlp>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataPemeriksaanulpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPemeriksaanulpBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pengujian : TTransPenerimaanUlp){
            with(binding){
                txtNoPengiriman.text = pengujian.noPengiriman
                txtStatusPenerimaan.text = pengujian.tempStatusPenerimaan
                txtVendorAsal.text = pengujian.noPermintaan
                txtStatusPemeriksaan.text = pengujian.tempStatusPemeriksaan

                if (pengujian.isDonePemeriksaan == 1){
                    if (pengujian.isDone == 1){
                        ivDoc.setImageResource(R.drawable.ic_input_doc_done)
                    }else{
                        ivDoc.setImageResource(R.drawable.ic_input_doc_done)
                    }
                }

                ivDoc.setOnClickListener { listener.onClick(pengujian) }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: TTransPenerimaanUlp)
    }
}