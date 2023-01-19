package dev.iconpln.mims

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListTanggalPengujianAdapter(private val listTanggalPengujian: ArrayList<PengujianRecycler>): RecyclerView.Adapter<ListTanggalPengujianAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTanggalUji: TextView = itemView.findViewById(R.id.txt_tanggalPengujian)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_pengujian, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (tanggalPengujian) = listTanggalPengujian[position]
        holder.tvTanggalUji.text = tanggalPengujian
    }

    override fun getItemCount(): Int = listTanggalPengujian.size
    }
