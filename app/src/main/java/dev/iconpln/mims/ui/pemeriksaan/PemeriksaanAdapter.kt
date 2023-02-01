package dev.iconpln.mims.ui.pemeriksaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding

class PemeriksaanAdapter(val lisModels: MutableList<TPemeriksaan>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PemeriksaanAdapter.ViewHolder>() {

    fun setPeList(pe: List<TPemeriksaan>){
        lisModels.clear()
        lisModels.addAll(pe)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataPenerimaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPenerimaanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : TPemeriksaan){
            with(binding){
                txtDeliveryOrder.text = po.noPemeriksaan
                txtStatusPenerimaan.text = po.doStatus
                txtStatusPemeriksaan.text = "Belum Diperiksa"
                txtVendorAsal.text = po.planCodeNo
                txtTglKirim.text = "Tanggal dikirim ${po.createdDate}"
                txtUnitTujuan.text = po.plantName

                if(po.isDone == 1){
                    isChecked.visibility = View.VISIBLE
                }else{
                    isChecked.visibility = View.GONE
                }
            }

            itemView.setOnClickListener { listener.onClick(po) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPemeriksaan)
    }
}