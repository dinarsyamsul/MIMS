package dev.iconpln.mims.ui.role.pabrikan.arttribute_material

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.databinding.ItemDataMaterialPabrikanBinding
import dev.iconpln.mims.databinding.ItemDetailMaterialPabrikanBinding

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
        val itemBinding = ItemDataMaterialPabrikanBinding.inflate(
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

    class ListViewHolder(val itemBinding: ItemDataMaterialPabrikanBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: DataItemMaterial) {
            with(itemBinding) {
                txtNoBatch.text = item.noProduksi
                txtSerialNumber.text = item.nomorMaterial
                txtExcel.text = item.sources
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemMaterial)
    }
}

class ListDetailMaterialAdapter() : RecyclerView.Adapter<ListDetailMaterialAdapter.ListViewHolder>() {

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
        val itemBinding = ItemDetailMaterialPabrikanBinding.inflate(
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

    class ListViewHolder(val itemBinding: ItemDetailMaterialPabrikanBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: DataItemMaterial) {
            with(itemBinding) {
                txtNoProduksi.text = item.noProduksi
                txtSerialNumber.text = item.serialNumber
                txtTglProduksi.text = item.tglProduksi
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemMaterial)
    }
}