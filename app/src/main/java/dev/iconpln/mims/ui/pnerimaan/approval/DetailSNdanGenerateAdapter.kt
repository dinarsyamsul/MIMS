package dev.iconpln.mims.ui.pnerimaan.approval

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.databinding.ItemDataApprovalBinding
import dev.iconpln.mims.databinding.ItemDataDetailGenerateSnDanAktivasiBinding

class DetailSNdanGenerateAdapter (private val myDataset: List<String>) :
    RecyclerView.Adapter<DetailSNdanGenerateAdapter.MyViewHolder>() {

    var isBtnPilihSelected = false
    var isCheckboxVisibleList = MutableList(myDataset.size) {false}
    var isSubmitButtonVisible = false

    fun toggleButtonSelection(){
        isBtnPilihSelected = !isBtnPilihSelected
        notifyDataSetChanged()
    }

    fun updateCheckboxVisibility(){
        isSubmitButtonVisible = isCheckboxVisibleList.any {it}
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailSNdanGenerateAdapter.MyViewHolder {
        val binding = ItemDataDetailGenerateSnDanAktivasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailSNdanGenerateAdapter.MyViewHolder, position: Int) {
        if (isCheckboxVisibleList[position]){
            holder.binding.checkbox.visibility = View.VISIBLE
        } else {
            holder.binding.checkbox.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    inner class MyViewHolder(val binding: ItemDataDetailGenerateSnDanAktivasiBinding): RecyclerView.ViewHolder(binding.root){

    }

}