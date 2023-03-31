package dev.iconpln.mims.ui.pemakaian

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TListSnMaterialPemakaianUlp
import dev.iconpln.mims.data.local.database.TListSnMaterialPenerimaanUlp
import dev.iconpln.mims.data.local.database.TMonitoringPermintaan
import dev.iconpln.mims.data.local.database.TMonitoringPermintaanDetail
import dev.iconpln.mims.data.local.database.TMonitoringSnMaterial
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.databinding.ItemDataMonitoringPermintaanBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPermintaanDetailBinding
import dev.iconpln.mims.databinding.ItemDataMonitoringPurchaseBinding
import dev.iconpln.mims.databinding.ItemSnMonitoringBinding
import dev.iconpln.mims.databinding.ItemSnPemeriksaanUlpBinding

class PemakaianUlpSnAdapter(val lisModels: MutableList<TListSnMaterialPemakaianUlp>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PemakaianUlpSnAdapter.ViewHolder>() {

    fun setTmsList(tms: List<TListSnMaterialPemakaianUlp>){
        lisModels.clear()
        lisModels.addAll(tms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PemakaianUlpSnAdapter.ViewHolder {
        val binding = ItemSnMonitoringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PemakaianUlpSnAdapter.ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemSnMonitoringBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tms : TListSnMaterialPemakaianUlp){
            with(binding){
                txtSnMaterial.text = tms.noSerialNumber
                btnDelete.setOnClickListener { listener.onClick(tms) }

            }
        }
    }

    interface OnAdapterListener{
        fun onClick(tms: TListSnMaterialPemakaianUlp)
    }
}