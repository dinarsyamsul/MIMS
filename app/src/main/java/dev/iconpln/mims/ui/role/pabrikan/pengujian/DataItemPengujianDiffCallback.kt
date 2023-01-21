package dev.iconpln.mims.ui.role.pabrikan.pengujian

import androidx.recyclerview.widget.DiffUtil
import dev.iconpln.mims.data.remote.response.DataItemPengujian

class DataItemPengujianDiffCallback(
    private val mOldDataPengujianList: List<DataItemPengujian>,
    private val mNewDataPengujianList: List<DataItemPengujian>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldDataPengujianList.size
    }

    override fun getNewListSize(): Int {
        return mNewDataPengujianList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDataPengujianList[oldItemPosition].noPengujian == mNewDataPengujianList[newItemPosition].noPengujian
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldDataPengujianList[oldItemPosition]
        val newEmployee = mNewDataPengujianList[newItemPosition]

        return oldEmployee.noPengujian == newEmployee.noPengujian &&
                oldEmployee.namaKategori == newEmployee.namaKategori
    }
}