package dev.iconpln.mims.ui.pemeriksaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ItemDataPemeriksaanBinding
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding

class PemeriksaanAdapter(val lisModels: MutableList<TPemeriksaan>,
                         var listener: OnAdapterListener,
                         var listenerDoc: OnAdapterListenerDoc,
                         var listenerRate: OnAdapterListenerRate)
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
        val binding = ItemDataPemeriksaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPemeriksaanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pe : TPemeriksaan){
            with(binding){
                txtDeliveryOrder.text = pe.noDoSmar
                txtStatusPenerimaan.text = pe.doStatus
                txtStatusPemeriksaan.text = "Belum Diperiksa"
                txtVendorAsal.text = pe.planCodeNo
                txtTglKirim.text = "Tgl ${pe.createdDate}"
                txtUnitTujuan.text = pe.plantName

                ivInputPerson.setOnClickListener { listener.onClick(pe) }
                ivDoc.setOnClickListener { listenerDoc.onClick(pe) }
                ivDelivery.setOnClickListener { listenerRate.onClick(pe) }

                if (pe.state == 2){
                    ivDoc.setImageResource(R.drawable.ic_input_doc_active)
                    ivDelivery.setImageResource(R.drawable.ic_input_delivery_to_rating_active)
                }

                if (pe.isDone == 1){
                    ivDoc.setImageResource(R.drawable.ic_input_doc_done)
                    ivDelivery.setImageResource(R.drawable.ic_input_delivery_to_rating_done)
                    ivInputPerson.setImageResource(R.drawable.ic_input_petugas_done)
                }


//                if(po.isDone == 1){
//                    isChecked.visibility = View.VISIBLE
//                }else{
//                    isChecked.visibility = View.GONE
//                }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPemeriksaan)
    }

    interface OnAdapterListenerDoc{
        fun onClick(po: TPemeriksaan)
    }

    interface OnAdapterListenerRate{
        fun onClick(po: TPemeriksaan)
    }
}