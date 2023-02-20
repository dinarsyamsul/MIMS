package dev.iconpln.mims

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.iconpln.mims.data.local.database.DaoSession
import dev.iconpln.mims.data.local.database.TPhoto
import dev.iconpln.mims.databinding.ItemPhotoBinding

class AddPhotoAdapter(
    val lisModels: MutableList<TPhoto>,
    var listener: OnAdapterListener,
    var hideAddPhoto: Boolean
)
    : RecyclerView.Adapter<AddPhotoAdapter.ViewHolder>() {

    fun setPhotoList(photo: List<TPhoto>){
        lisModels.clear()
        lisModels.addAll(photo)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lisModels[position])
    }

    override fun getItemCount(): Int = lisModels.size

    inner class ViewHolder(val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(po : TPhoto){
            with(binding){
                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(po.path))
                ivPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                btnTambahPhoto.setOnClickListener { listener.onClick(po) }

                if (hideAddPhoto){
                    if (lisModels.size == po.photoNumber){
                        btnTambahPhoto.visibility = View.VISIBLE
                    }else{
                        btnTambahPhoto.visibility = View.GONE
                    }
                }else{
                    btnTambahPhoto.visibility = View.GONE
                }

            }
        }
    }

    interface OnAdapterListener{
        fun onClick(po: TPhoto)
    }
}