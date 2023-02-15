package dev.iconpln.mims.ui.rating.detail_rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.TPemeriksaanDetail
import dev.iconpln.mims.data.local.database.TPosPenerimaan
import dev.iconpln.mims.data.local.database.TRating
import dev.iconpln.mims.databinding.ItemDataPenerimaanBinding
import dev.iconpln.mims.databinding.ItemDataRatingBinding
import dev.iconpln.mims.databinding.ItemPackagingBinding
import dev.iconpln.mims.databinding.ItemRatingBinding

class RatingPenyediaAdapter(val lisModels: MutableList<TRating>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<RatingPenyediaAdapter.ViewHolder>() {

    fun setRatList(rat: List<TRating>){
        lisModels.clear()
        lisModels.addAll(rat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemRatingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rat : TRating){
            with(binding){
                if (rat.isActive == 1){
                    btnRate.setImageResource(R.drawable.ic_star_true)
                }else{
                    btnRate.setImageResource(R.drawable.ic_rating)
                }
                btnRate.setOnClickListener { listener.onClick(rat) }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TRating)
    }
}