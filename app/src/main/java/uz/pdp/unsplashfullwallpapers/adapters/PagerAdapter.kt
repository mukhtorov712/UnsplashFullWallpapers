package uz.pdp.unsplashfullwallpapers.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.pdp.unsplashfullwallpapers.room.entity.TopicModel
import uz.pdp.unsplashfullwallpapers.ui.photo.PagerFragment

class PagerAdapter(
    private val list: List<TopicModel>,
    private val order_by: String,
    fragment: Fragment
) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return PagerFragment().apply {
            arguments = Bundle().apply {
                putString("id", list[position].id)
                putString("order", order_by)
            }
        }
    }
}