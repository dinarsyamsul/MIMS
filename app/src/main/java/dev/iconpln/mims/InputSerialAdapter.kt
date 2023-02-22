package dev.iconpln.mims

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class InputSerialAdapter (private val listInput: ArrayList<String>): RecyclerView.Adapter<InputSerialAdapter.ListInputViewHolder>(){
    class ListInputViewHolder(inputView: View) : RecyclerView.ViewHolder(inputView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListInputViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_input_sn_material, parent, false)
        return ListInputViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListInputViewHolder, position: Int) {
        val data = listInput[position]
    }

    override fun getItemCount(): Int {
        return listInput.size
    }
}