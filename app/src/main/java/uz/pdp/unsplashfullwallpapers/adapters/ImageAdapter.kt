package uz.pdp.unsplashfullwallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.unsplashfullwallpapers.databinding.ItemImageRvBinding
import uz.pdp.unsplashfullwallpapers.model.ImageModel

class ImageAdapter(val listener: OnImageClickListener) :
    PagingDataAdapter<ImageModel, ImageAdapter.Vh>(MyDiffUtil()) {


    class MyDiffUtil : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class Vh(private val itemRvBinding: ItemImageRvBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(imageModel: ImageModel) {
            itemRvBinding.apply {
                Picasso.get().load(imageModel.urls.thumb).into(image)
            }
            itemView.setOnClickListener {
                listener.onImageClick(imageModel)
            }
        }
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemImageRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    interface OnImageClickListener {
        fun onImageClick(imageModel: ImageModel)
    }


}