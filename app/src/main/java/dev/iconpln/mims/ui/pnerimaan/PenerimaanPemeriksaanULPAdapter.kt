package dev.iconpln.mims.ui.pnerimaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.databinding.ItemDataPenerimaanulpBinding

class PenerimaanPemeriksaanULPAdapter(private val listData: ArrayList<String>) : RecyclerView.Adapter<PenerimaanPemeriksaanULPAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDataPenerimaanulpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listData[holder.bindingAdapterPosition])
        }
    }

    class ListViewHolder(var binding: ItemDataPenerimaanulpBinding): RecyclerView.ViewHolder(binding.root) {
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }
}