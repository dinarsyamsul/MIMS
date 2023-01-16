package dev.iconpln.mims

import androidx.recyclerview.widget.DiffUtil
import dev.iconpln.mims.data.remote.response.DataItemMaterial

class DataItemMaterialDiffCallback(
    private val mOldDataMaterialList: List<DataItemMaterial>,
    private val mNewDataMaterialList: List<DataItemMaterial>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldDataMaterialList.size
    }

    override fun getNewListSize(): Int {
        return mNewDataMaterialList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDataMaterialList[oldItemPosition].nomorMaterial == mNewDataMaterialList[newItemPosition].nomorMaterial
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldDataMaterialList[oldItemPosition]
        val newEmployee = mNewDataMaterialList[newItemPosition]

        return oldEmployee.nomorMaterial == newEmployee.nomorMaterial &&
                oldEmployee.noProduksi == newEmployee.noProduksi
    }
}