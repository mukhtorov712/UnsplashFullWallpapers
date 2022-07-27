package uz.pdp.unsplashfullwallpapers.ui.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.pdp.unsplashfullwallpapers.R
import uz.pdp.unsplashfullwallpapers.adapters.FilterAdapter
import uz.pdp.unsplashfullwallpapers.adapters.LikeAdapter
import uz.pdp.unsplashfullwallpapers.databinding.FragmentSearchBinding
import uz.pdp.unsplashfullwallpapers.model.ImageModel
import uz.pdp.unsplashfullwallpapers.room.entity.ImageEntity
import uz.pdp.unsplashfullwallpapers.ui.viewmodel.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var likeAdapter: LikeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = SearchViewModel()

        val query = arguments?.getString("query") ?: ""

        searchViewModel.fetchSearch(query).observe(viewLifecycleOwner, {
            likeAdapter = LikeAdapter(it, object : LikeAdapter.OnImageClickListener {
                override fun onImageClick(imageModel: ImageModel) {
                    findNavController().navigate(R.id.photoFragment, Bundle().apply {
                        putSerializable("image", imageModel)
                    })
                }
            })
            binding.rv.adapter = likeAdapter
        })

    }
}