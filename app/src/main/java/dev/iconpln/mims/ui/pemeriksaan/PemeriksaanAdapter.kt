package dev.iconpln.mims.ui.pemeriksaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.R
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPemeriksaan
import dev.iconpln.mims.data.local.database.TPemeriksaanDao
import dev.iconpln.mims.data.local.database.TPemeriksaanDetailDao
import dev.iconpln.mims.databinding.ItemDataPeemeriksaanBinding

class PemeriksaanAdapter(
    val lisModels: MutableList<TPemeriksaan>,
    var listener: OnAdapterListener,
    var listenerDoc: OnAdapterListenerDoc, var daoSession: DaoSession)
    : RecyclerView.Adapter<PemeriksaanAdapter.ViewHolder>() {

    fun setPeList(pe: List<TPemeriksaan>){
        lisModels.clear()
        lisModels.addAll(pe)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDataPeemeriksaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemDataPeemeriksaanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pe : TPemeriksaan){
            with(binding){
                txtDeliveryOrder.text = pe.noDoSmar
                txtNoPoSap.text = if (pe.poSapNo.isNullOrEmpty()) "-" else pe.poSapNo
                txtTglKirim.text = "Tgl ${pe.createdDate}"
                txtUnitTujuan.text = pe.plantName
                txtNoPemeriksaan.text = if (pe.noPemeriksaan.isNullOrEmpty()) "-" else pe.noPemeriksaan

                ivInputPerson.setOnClickListener { listener.onClick(pe) }
                ivDoc.setOnClickListener { listenerDoc.onClick(pe) }

                var listPemDetail = daoSession.tPemeriksaanDetailDao.queryBuilder()
                    .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.eq(pe.noPemeriksaan))
                    .where(TPemeriksaanDetailDao.Properties.IsPeriksa.eq(1))
                    .where(TPemeriksaanDetailDao.Properties.StatusPemeriksaan.eq("BELUM DIPERIKSA"))
                    .where(TPemeriksaanDetailDao.Properties.IsComplaint.eq(0))
                    .where(TPemeriksaanDetailDao.Properties.NoPemeriksaan.notEq(""))
                    .list()

                var listPem = daoSession.tPemeriksaanDao.queryBuilder()
                    .where(TPemeriksaanDao.Properties.NoDoSmar.eq(pe.noDoSmar)).list().get(0)

                if (pe.namaKetua.isNotEmpty() && pe.isDone == 0){
                    ivInputPerson.setImageResource(R.drawable.ic_input_petugas_done)
                    ivDoc.setImageResource(R.drawable.ic_input_doc_active)
                }else if (pe.namaKetua.isNullOrEmpty() && pe.isDone == 0){
                    ivInputPerson.setImageResource(R.drawable.ic_input_petugas_active)
                    ivDoc.setImageResource(R.drawable.ic_input_doc_false)
                }

                if (pe.isDone == 1 || listPemDetail.size == 0){
                    listPem.statusPemeriksaan = "SUDAH DIPERIKSA"
                    daoSession.tPemeriksaanDao.update(listPem)
                    txtStatusPemeriksaan.text = "SUDAH DIPERIKSA"

                    ivDoc.setImageResource(R.drawable.ic_input_doc_done)
                    ivInputPerson.setImageResource(R.drawable.ic_input_petugas_done)
                }else{
                    txtStatusPemeriksaan.text = "SEDANG DIPERIKSA"
                }

//                if(po.isDone == 1){
//                    isChecked.visibility = View.VISIBLE
//                }else{
//                    isChecked.visibility = View.GONE
//                }
            }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPemeriksaan)
    }

    interface OnAdapterListenerDoc{
        fun onClick(po: TPemeriksaan)
    }
}