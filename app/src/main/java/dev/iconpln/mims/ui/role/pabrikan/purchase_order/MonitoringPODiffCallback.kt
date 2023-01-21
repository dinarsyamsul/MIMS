package dev.iconpln.mims.ui.role.pabrikan.purchase_order

import androidx.recyclerview.widget.DiffUtil
import dev.iconpln.mims.data.remote.response.DataMonitoringPO

class MonitoringPODiffCallback(
    private val mOldMonitoringPO: List<DataMonitoringPO>,
    private val mNewMonitoringPO: List<DataMonitoringPO>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldMonitoringPO.size
    }

    override fun getNewListSize(): Int {
        return mNewMonitoringPO.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldMonitoringPO[oldItemPosition].nomorMaterial == mNewMonitoringPO[newItemPosition].nomorMaterial
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldMonitoringPO[oldItemPosition]
        val newEmployee = mNewMonitoringPO[newItemPosition]

        return oldEmployee.nomorMaterial == newEmployee.nomorMaterial &&
                oldEmployee.kdPabrikan == newEmployee.kdPabrikan
    }
}