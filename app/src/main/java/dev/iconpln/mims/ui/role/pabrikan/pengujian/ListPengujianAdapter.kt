package dev.iconpln.mims.ui.role.pabrikan.pengujian

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemPengujian
import dev.iconpln.mims.databinding.ItemPengujianBinding
import dev.iconpln.mims.ui.role.pabrikan.purchase_order.ListNoPoAdapter

class ListPengujianAdapter() : RecyclerView.Adapter<ListPengujianAdapter.ListViewHolder>() {

    private val listPengujian = ArrayList<DataItemPengujian>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: List<DataItemPengujian>) {
        val diffCallback = DataItemPengujianDiffCallback(listPengujian, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listPengujian.clear()
        listPengujian.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback?) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemPengujianBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPengujian[position])

//        holder.itemBinding.btnDetail.setOnClickListener {
//            onItemClickCallback?.onItemClicked(listPengujian[holder.bindingAdapterPosition])
//        }
    }

    override fun getItemCount(): Int = listPengujian.size

    class ListViewHolder(val itemBinding: ItemPengujianBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: DataItemPengujian) {
            with(itemBinding) {
                txtTanggalPengujian.text = item.tanggalUji
                txtSerialNumber.text = item.noPengujian
                txtIsikategori.text = item.namaKategori
                txtIsiJumlah.text = item.qtyMaterial
                txtIsiSatuan.text = item.unit
                statusUji.text = item.statusUji
                Log.d("ListPengujianAdapter", "cek di adapter pengujian ${item.tanggalUji}")
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemPengujian)
    }
}