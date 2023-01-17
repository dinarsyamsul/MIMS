package dev.iconpln.mims.ui.role.pabrikan.purchase_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.data.remote.response.DataMonitoringPO
import dev.iconpln.mims.databinding.ItemDataMonitoringPurchaseBinding

class ListNoPoAdapter() : RecyclerView.Adapter<ListNoPoAdapter.ListViewHolder>() {

    private val listMaterial = ArrayList<DataMonitoringPO>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: List<DataMonitoringPO>) {
        val diffCallback = MonitoringPODiffCallback(listMaterial, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listMaterial.clear()
        listMaterial.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback?) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemDataMonitoringPurchaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMaterial[position])
    }

    override fun getItemCount(): Int = listMaterial.size

    class ListViewHolder(val itemBinding: ItemDataMonitoringPurchaseBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: DataMonitoringPO) {
            with(itemBinding) {
                txtSerialNumber.text = item.noPurchaseOrder
                txtIsiUnit.text = item.unit
                txtIsiStore.text = item.storLoc
                txtIsiKuantitas.text = item.qtyPo
                txtIsiPlant.text = item.plant
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemMaterial)
    }
}