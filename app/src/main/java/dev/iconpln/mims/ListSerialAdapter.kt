package dev.iconpln.mims

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemMaterial
import dev.iconpln.mims.databinding.ActivityListDataMaterialPabrikanBinding
import dev.iconpln.mims.databinding.ListItemBinding
import org.w3c.dom.Text

class ListSerialAdapter(): RecyclerView.Adapter<ListSerialAdapter.ListViewHolder>() {

    private val listMaterial = ArrayList<DataItemMaterial>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: List<DataItemMaterial>){
        val diffCallback = DataItemMaterialDiffCallback(listMaterial, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listMaterial.clear()
        listMaterial.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback?){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ActivityListDataMaterialPabrikanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMaterial[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listMaterial[holder.bindingAdapterPosition])
        }

    }

    override fun getItemCount(): Int = listMaterial.size

    class ListViewHolder(private val itemBinding: ActivityListDataMaterialPabrikanBinding): RecyclerView.ViewHolder(itemBinding.root)  {

        fun bind(item: DataItemMaterial){
            with(itemBinding){
                txtSerialNumber.text = item.noProduksi
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemMaterial)
    }
}
