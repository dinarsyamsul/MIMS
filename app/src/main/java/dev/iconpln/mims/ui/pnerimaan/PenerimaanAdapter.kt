package dev.iconpln.mims.ui.pnerimaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPosDetailPenerimaanDao
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding

class PenerimaanAdapter(val lisModels: MutableList<TPosPenerimaan>,
                        var listener: OnAdapterListener,
                        var listenerDoc: OnAdapterListenerDoc,
                        var listenerRate: OnAdapterListenerRate,
                        var daoSession: DaoSession)
    : RecyclerView.Adapter<PenerimaanAdapter.ViewHolder>() {

    fun setData(pe: List<TPosPenerimaan>){
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
        fun bind(pe : TPosPenerimaan){
            with(binding){
                val listDetailPen = daoSession.tPosDetailPenerimaanDao.queryBuilder()
                    .where(TPosDetailPenerimaanDao.Properties.NoDoSmar.eq(pe.noDoSmar))
                    .where(TPosDetailPenerimaanDao.Properties.IsDone.eq(0)).list()

                txtDeliveryOrder.text = pe.noDoSmar
                txtStatusPenerimaan.text = if (pe.doStatus.isNullOrEmpty()) "-" else pe.doStatus
                txtStatusPemeriksaan.text = "Belum Diperiksa"
                txtVendorAsal.text = pe.planCodeNo
                txtTglKirim.text = "Tgl ${pe.createdDate}"
                txtUnitTujuan.text = pe.plantName

                ivInputPerson.setOnClickListener { listener.onClick(pe) }
                ivDoc.setOnClickListener { listenerDoc.onClick(pe) }
                ivDelivery.setOnClickListener { listenerRate.onClick(pe) }

                if (!pe.tanggalDiterima.isNullOrEmpty()){
                    if (listDetailPen.isNullOrEmpty()){
                        ivDoc.setImageResource(R.drawable.ic_input_doc_done)
                        ivDelivery.setImageResource(R.drawable.ic_input_delivery_to_rating_active)

                    }else{
                        ivDoc.setImageResource(R.drawable.ic_input_doc_active)
                    }

                    ivInputPerson.setImageResource(R.drawable.ic_input_petugas_done)
//                    ivDelivery.setImageResource(R.drawable.ic_input_delivery_to_rating_active)
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
        fun onClick(po: TPosPenerimaan)
    }

    interface OnAdapterListenerDoc{
        fun onClick(po: TPosPenerimaan)
    }

    interface OnAdapterListenerRate{
        fun onClick(po: TPosPenerimaan)
    }
}