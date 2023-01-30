package dev.iconpln.mims.ui.pengiriman

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosSns
import dev.iconpln.mims.databinding.ItemDetailPengirimanBinding
import dev.iconpln.mims.databinding.ItemPengirimanBinding

class ListPengirimanAdapter(val lisModels: MutableList<TPos>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<ListPengirimanAdapter.ViewHolder>() {

    fun setPengirimanList(mat: List<TPos>){
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPengirimanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemPengirimanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : TPos){
            with(binding){
                txtIsiNopengiriman.text = data.noDoSmar
                txtIsiNopo.text = data.poSapNo
//                txtIsinoDOpengiriman.text = data
                txtIsiunitpengiriman.text = data.plantName
                txtIsikuantitaspengiriman.text = data.total
//                txtStatuspengiriman.text =
            }

            itemView.setOnClickListener { listener.onClick(data) }
        }
    }

    interface OnAdapterListener{
        fun onClick(pengujian: TPos)
    }
}

class ListDetailPengirimanAdapter(val lisModels: MutableList<TPosSns>, var listener: OnAdapterListener)
    : RecyclerView.Adapter<ListDetailPengirimanAdapter.ViewHolder>() {

    fun setDetailPengirimanList(mat: List<TPosSns>) {
        lisModels.clear()
        lisModels.addAll(mat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemDetailPengirimanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDetailPengirimanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TPosSns) {
            with(binding) {
                txtSerialNumber.text = data.noSerial
                txtTglProduksi.text = data.tglProduksi
                txtNoMaterial.text = data.noMatSap
                txtIsiMeteorologi.text = data.noSertMeterologi
                txtIsiBatch.text = data.noProduksi
//                txtIsiPackaging.text = data.
                txtGaransi.text = data.masaGaransi
                txtIsiSpln.text = data.spln
                txtIsiKategori.text = data.namaKategoriMaterial
            }

            itemView.setOnClickListener { listener.onClick(data) }
        }
    }

    interface OnAdapterListener {
        fun onClick(pengujian: TPosSns)
    }
}