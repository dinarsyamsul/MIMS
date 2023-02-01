package dev.iconpln.mims.ui.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding
import dev.iconpln.mims.databinding.ItemDataRatingBinding

class RatingAdapter(val lisModels: MutableList<TPemeriksaan>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    fun setRatingList(rat: List<TPemeriksaan>){
        lisModels.clear()
        lisModels.addAll(rat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : TPemeriksaan){
            with(binding){
                txtDeliveryOrder.text = po.noDoSmar
                txtStatusPenerimaan.text = po.doStatus
                txtStatusPemeriksaan.text = "-"
                txtVendorAsal.text = po.plantName
                txtTglKirim.text = "Tanggal dikirim ${po.createdDate}"
                txtUnitTujuan.text = "-"

                if(po.isDone == 1){
                    ivIsRating.setBackgroundResource(R.drawable.ic_israting)
                }else{
                    ivIsRating.setBackgroundResource(R.drawable.ic_is_not_rating)                }
            }

            itemView.setOnClickListener { listener.onClick(po) }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPemeriksaan)
    }
}