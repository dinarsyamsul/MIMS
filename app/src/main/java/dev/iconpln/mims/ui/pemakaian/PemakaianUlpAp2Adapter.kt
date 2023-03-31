package dev.iconpln.mims.ui.pemakaian

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.PemakaianUlpData
import dev.iconpln.mims.data.remote.PemakaianUlpMandiriData
import dev.iconpln.mims.databinding.ItemDataDetailPemakaianUlpMandiriBinding
import dev.iconpln.mims.databinding.ItemDataPemakaianUlpBinding

class PemakaianUlpAp2Adapter(val lisModels: MutableList<PemakaianUlpMandiriData>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<PemakaianUlpAp2Adapter.ViewHolder>() {

    fun setPengujianList(mat: List<PemakaianUlpMandiriData>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataDetailPemakaianUlpMandiriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataDetailPemakaianUlpMandiriBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pengujian : PemakaianUlpMandiriData){
            with(binding){
                txtJumlahPemakaian.text = pengujian.jumlahPemakaian
                txtSatuan.text = pengujian.satuan
                txtTglReservasi.text = pengujian.namaMaterial
                txtJumlahReservasi.text = pengujian.jumlahReservasi
                txtNoMeter.text = pengujian.noMeter
                txtJumlahPemakaian.text = pengujian.jumlahPemakaian
                txtValuationType.text = pengujian.valuationType

                lblNoDo.setOnClickListener { listener.onClick(pengujian) }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: PemakaianUlpMandiriData)
    }
}