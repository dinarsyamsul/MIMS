package dev.iconpln.mims.ui.pengiriman

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.TPos
import dev.iconpln.mims.data.local.database.TPosSns
import dev.iconpln.mims.databinding.ItemDetailPengirimanBinding
import dev.iconpln.mims.databinding.ItemDataPengirimanBinding

class ListPengirimanAdapter(val context: Context, val lisModels: MutableList<TPos>, var listener: OnAdapterListener)
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
        val binding = ItemDataPengirimanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPengirimanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : TPos){
            with(binding){
                txtNoPengiriman.text = data.noDoSmar
                txtNoPo.text = if(data.poSapNo.isNullOrEmpty()) "-" else data.poSapNo
                txtNoDo.text = data.noDoMims
                txtUnit.text = data.plantName
                txtQuantity.text = data.total
                btnDetail.setOnClickListener {
                    val intent = Intent(context, DetailPengirimanActivity::class.java)
                    intent.putExtra(DetailPengirimanActivity.EXTRA_NO_PENGIRIMAN, data.noDoSmar)
                    context.startActivity(intent)
                }
                btnLokasi.setOnClickListener {
                    val intent = Intent(context, UpdateLokasiActivity::class.java)
                    intent.putExtra(UpdateLokasiActivity.EXTRA_NO_DOMIMS, data.noDoMims)
                    context.startActivity(intent)
                }
                
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