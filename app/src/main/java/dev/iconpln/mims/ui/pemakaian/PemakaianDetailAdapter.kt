package dev.iconpln.mims.ui.pemakaian

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.*
import dev.iconpln.mims.data.remote.PemakaianUlpData
import dev.iconpln.mims.databinding.ItemDataDetailPemakaianUlpBinding
import dev.iconpln.mims.databinding.ItemDataPemakaianUlpBinding

class PemakaianDetailAdapter(val lisModels: MutableList<TTransPemakaianDetail>,
                             var listener: OnAdapterListener, val daoSession: DaoSession)
    : RecyclerView.Adapter<PemakaianDetailAdapter.ViewHolder>() {

    fun setpemakaianList(mat: List<TTransPemakaianDetail>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataDetailPemakaianUlpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataDetailPemakaianUlpBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pemakaian : TTransPemakaianDetail){
            with(binding){

                if (pemakaian.isDone == 1){
                    ivDelivery.setImageResource(R.drawable.ic_input_doc_done)
                }else{
                    ivDelivery.setImageResource(R.drawable.ic_input_doc_active)
                }

                if (pemakaian.isActive == 0){
                    txtJumlahPemakaian.text = pemakaian.qtyPemakaian.toString()
                    txtJumlahReservasi.text = pemakaian.qtyReservasi.toString()
                }else{
                    txtJumlahPemakaian.text = pemakaian.qtyPemakaian.toInt().toString()
                    txtJumlahReservasi.text = pemakaian.qtyReservasi.toInt().toString()
                }

                txtSatuan.text = pemakaian.unit
                txtNamaMaterial.text = pemakaian.namaMaterial
                txtNoMeter.text = pemakaian.noMeter
                txtNoMaterial.text = pemakaian.nomorMaterial
                txtValuationType.text = pemakaian.valuationType

                ivDelivery.setOnClickListener { listener.onClick(pemakaian) }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(pemakaian: TTransPemakaianDetail)
    }
}