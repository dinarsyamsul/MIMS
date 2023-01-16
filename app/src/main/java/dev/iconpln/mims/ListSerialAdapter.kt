package dev.iconpln.mims

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ListSerialAdapter(private val listSerial: ArrayList<BatchSerial>): RecyclerView.Adapter<ListSerialAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {

        var tvBatch: TextView = itemView.findViewById(R.id.txt_noBatch)
        var tvSerialNumber: TextView = itemView.findViewById(R.id.txt_serialNumber)
        var tvExcel: TextView = itemView.findViewById(R.id.txt_excel)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_list_data_material_pabrikan, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (noBatch, serialNumber, excel) = listSerial[position]
        holder.tvBatch.text = noBatch
        holder.tvExcel.text = excel
        holder.tvSerialNumber.text = serialNumber

    }

    override fun getItemCount(): Int = listSerial.size
}
