package dev.iconpln.mims

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.utils.NoPoSerial

class ListNoPoAdapter(private val listNoPo: ArrayList<NoPoSerial>): RecyclerView.Adapter<ListNoPoAdapter.ListViewHolder>(){
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvnoPo: TextView = itemView.findViewById(R.id.txt_noPo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_list_monitoring_purchase, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (noPoSerial) = listNoPo[position]
        holder.tvnoPo.text = noPoSerial
    }

    override fun getItemCount(): Int = listNoPo.size
}