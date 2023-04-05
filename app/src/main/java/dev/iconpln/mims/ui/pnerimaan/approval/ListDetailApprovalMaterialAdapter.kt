package dev.iconpln.mims.ui.pnerimaan.approval

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemMaterialRegistrasiByDate
import dev.iconpln.mims.databinding.ItemDataDetailGenerateSnDanAktivasiBinding

class ListDetailApprovalMaterialAdapter :
    RecyclerView.Adapter<ListDetailApprovalMaterialAdapter.ListViewHolder>() {

    private val listSnMaterial = ArrayList<DataItemMaterialRegistrasiByDate>()
    var isSelectionMode = false
    val checkedItems = SparseBooleanArray()

    fun setListMaterialByDate(listSn: List<DataItemMaterialRegistrasiByDate>) {
        this.listSnMaterial.clear()
        this.listSnMaterial.addAll(listSn)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): DataItemMaterialRegistrasiByDate {
        return listSnMaterial[position]
    }

    fun isItemChecked(position: Int): Boolean {
        return checkedItems.get(position, false)
    }

    fun toggleItemSelection(position: Int) {
        val isChecked = checkedItems.get(position, false)
        checkedItems.put(position, !isChecked)
        notifyItemChanged(position)
    }

    fun getCheckedItems(): List<DataItemMaterialRegistrasiByDate> {
        val checkedItemsData = ArrayList<DataItemMaterialRegistrasiByDate>()
        for (i in 0 until checkedItems.size()) {
            val position = checkedItems.keyAt(i)
            if (checkedItems.get(position)) {
                val item = getItem(position)
                checkedItemsData.add(item)
            }
        }
        return checkedItemsData
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListDetailApprovalMaterialAdapter.ListViewHolder {
        val binding = ItemDataDetailGenerateSnDanAktivasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ListViewHolder(binding)

        holder.itemView.setOnClickListener {
            if (isSelectionMode){
                val position = holder.bindingAdapterPosition
                holder.binding.checkbox.isChecked = !holder.binding.checkbox.isChecked
                checkedItems.put(position, holder.binding.checkbox.isChecked)
            }
        }

        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val position = holder.bindingAdapterPosition
            checkedItems.put(position, isChecked)
        }


        return holder
    }

    override fun onBindViewHolder(holder: ListDetailApprovalMaterialAdapter.ListViewHolder, position: Int) {
        holder.bind(listSnMaterial[position])
        holder.binding.checkbox.visibility = if (isSelectionMode) View.VISIBLE else  View.GONE
        holder.binding.checkbox.isChecked = checkedItems[position]
//        if (isCheckboxVisibleList[position]){
//            holder.binding.checkbox.visibility = View.VISIBLE
//        } else {
//            holder.binding.checkbox.visibility = View.GONE
//        }
    }

    override fun getItemCount(): Int {
        return listSnMaterial.size
    }

    inner class ListViewHolder(val binding: ItemDataDetailGenerateSnDanAktivasiBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: DataItemMaterialRegistrasiByDate) {
            with(binding) {
                txtSn.text = item.serialNumber
                txtNoMaterial.text = item.nomorMaterial
                txtTahun.text = item.tahun
                txtNoBatch.text = item.noProduksi
                txtVendor.text = item.vendor
                checkbox.isChecked = isItemChecked(bindingAdapterPosition)
            }
        }

        init {
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                checkedItems.put(bindingAdapterPosition, isChecked) // update the checkedItems
            }
        }
    }
}