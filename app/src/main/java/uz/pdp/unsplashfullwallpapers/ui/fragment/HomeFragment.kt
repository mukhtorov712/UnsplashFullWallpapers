package uz.pdp.unsplashfullwallpapers.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uz.pdp.unsplashfullwallpapers.adapters.PagerAdapter
import uz.pdp.unsplashfullwallpapers.databinding.FragmentHomeBinding
import uz.pdp.unsplashfullwallpapers.databinding.ItemTabBinding
import uz.pdp.unsplashfullwallpapers.room.database.AppDatabase
import uz.pdp.unsplashfullwallpapers.room.entity.TopicModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var pagerAdapter: PagerAdapter
    lateinit var appDatabase: AppDatabase
    lateinit var list: List<TopicModel>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        appDatabase = AppDatabase.getInstance(requireContext())
        list = appDatabase.topicDao().getAllTopic()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            pagerAdapter = PagerAdapter(list,"latest", this@HomeFragment)
            binding.viewPager.adapter = pagerAdapter

            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab, position ->
                val tabCount = tabLayout.tabCount
                val tabBinding = ItemTabBinding.inflate(layoutInflater)
                tabBinding.apply {
                    tv.text = list[position].title
                    if (tabCount == 0) {
                        circle.visibility = View.VISIBLE
                        tv.setTextColor(Color.WHITE)
                    } else {
                        circle.visibility = View.INVISIBLE
                        tv.setTextColor(Color.parseColor("#AFADAD"))
                    }
                    tab.customView = root
                }
            }.attach()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val itemTabBinding = ItemTabBinding.bind(tab!!.customView!!)
                    itemTabBinding.tv.setTextColor(Color.WHITE)
                    itemTabBinding.circle.visibility = View.VISIBLE
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    val itemTabBinding = ItemTabBinding.bind(tab!!.customView!!)
                    itemTabBinding.tv.setTextColor(Color.parseColor("#AFADAD"))
                    itemTabBinding.circle.visibility = View.INVISIBLE
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}