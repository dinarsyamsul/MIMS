package dev.iconpln.mims.ui.pnerimaan.approval

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.remote.response.DataItemMaterialAktivasi
import dev.iconpln.mims.databinding.ItemDataApprovalBinding

class ListApprovalMaterialAdapter : RecyclerView.Adapter<ListApprovalMaterialAdapter.ListViewHolder>() {
    private val listSnMaterial = ArrayList<DataItemMaterialAktivasi>()

    fun setListMaterialAktivasi(listSn: List<DataItemMaterialAktivasi>) {
        this.listSnMaterial.clear()
        this.listSnMaterial.addAll(listSn)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDataApprovalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSnMaterial.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSnMaterial[position])
    }

    inner class ListViewHolder(private val binding: ItemDataApprovalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemMaterialAktivasi) {
            with(binding) {
                txtTglRegistrasi.text = item.tglRegistrasi
                txtTotalProcessed.text = item.totalSnProcess.toString()
                txtTotalApproved.text = item.totalSnApprove.toString()
                txtTotalQty.text = item.totalSn.toString()
                txtStatusApproved.text = item.status

                btnDetail.setOnClickListener {
                    val intent = Intent(it.context, DetailApprovalMaterialActivity::class.java)
                    intent.putExtra(DetailApprovalMaterialActivity.EXTRA_TGL_REGISTRASI, item.tglRegistrasi)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}