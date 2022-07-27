package uz.pdp.unsplashfullwallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.unsplashfullwallpapers.databinding.ItemImageRvBinding
import uz.pdp.unsplashfullwallpapers.model.ImageModel

class LikeAdapter(val list: List<ImageModel>, val listener: OnImageClickListener) :
    RecyclerView.Adapter<LikeAdapter.Vh>() {


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


    interface OnImageClickListener {
        fun onImageClick(imageModel: ImageModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh =
        Vh(ItemImageRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size


}