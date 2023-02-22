package dev.iconpln.mims.ui.pemeriksaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.databinding.ItemDataPeemeriksaanBinding

class PemeriksaanAdapter(val lisModels: MutableList<TPemeriksaan>,
                         var listener: OnAdapterListener,
                         var listenerDoc: OnAdapterListenerDoc)
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
        val binding = ItemDataPeemeriksaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPeemeriksaanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pe : TPemeriksaan){
            with(binding){
                txtDeliveryOrder.text = pe.noDoSmar
                txtStatusPemeriksaan.text = "Belum Diperiksa"
                txtVendorAsal.text = pe.planCodeNo
                txtTglKirim.text = "Tgl ${pe.createdDate}"
                txtUnitTujuan.text = pe.plantName

                ivInputPerson.setOnClickListener { listener.onClick(pe) }
                ivDoc.setOnClickListener { listenerDoc.onClick(pe) }

                if (pe.isDone == 1){
                    ivDoc.setImageResource(R.drawable.ic_input_doc_done)
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
}