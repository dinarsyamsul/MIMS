package dev.iconpln.mims.ui.role.pabrikan.arttribute_material

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.databinding.ActivityListDataMaterialPabrikanBinding

class ListMaterialAdapter() : RecyclerView.Adapter<ListMaterialAdapter.ListViewHolder>() {

    private val listMaterial = ArrayList<DataItemMaterial>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: List<DataItemMaterial>) {
        val diffCallback = DataItemMaterialDiffCallback(listMaterial, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listMaterial.clear()
        listMaterial.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback?) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ActivityListDataMaterialPabrikanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMaterial[position])

        holder.itemBinding.btnDetail.setOnClickListener {
            onItemClickCallback?.onItemClicked(listMaterial[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = listMaterial.size

    class ListViewHolder(val itemBinding: ActivityListDataMaterialPabrikanBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: DataItemMaterial) {
            with(itemBinding) {
                txtNoBatch.text = item.noProduksi
                txtSerialNumber.text = item.serialNumber
                txtExcel.text = item.source
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemMaterial)
    }
}