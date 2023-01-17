package dev.iconpln.mims

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListTanggalAdapter(private val listTanggal: ArrayList<TanggalFilter>): RecyclerView.Adapter<ListTanggalAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTanggal: TextView = itemView.findViewById(R.id.txt_Hari)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_tanggal_card_view, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (tanggalMonitoring) = listTanggal[position]
        holder.tvTanggal.text = tanggalMonitoring
    }

    override fun getItemCount(): Int = listTanggal.size
}