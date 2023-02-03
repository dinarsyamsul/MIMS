package dev.iconpln.mims.ui.pnerimaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding
import dev.iconpln.mims.databinding.ItemDataRatingBinding

class PenerimaanAdapter(val lisModels: MutableList<TPosPenerimaan>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PenerimaanAdapter.ViewHolder>() {

    fun setPoList(po: List<TPosPenerimaan>){
        lisModels.clear()
        lisModels.addAll(po)
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
        fun bind(po : TPosPenerimaan){
            with(binding){
                txtDeliveryOrder.text = po.noDoSmar
                txtStatusPenerimaan.text = po.doStatus
                txtStatusPemeriksaan.text = "-"
                txtVendorAsal.text = po.plantName
                txtTglKirim.text = "Tanggal dikirim ${po.createdDate}"
                txtUnitTujuan.text = "-"

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
        fun onClick(po: TPosPenerimaan)
    }
}