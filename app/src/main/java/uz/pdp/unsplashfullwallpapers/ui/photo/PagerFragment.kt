package uz.pdp.unsplashfullwallpapers.ui.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.unsplashfullwallpapers.R
import uz.pdp.unsplashfullwallpapers.adapters.ImageAdapter
import uz.pdp.unsplashfullwallpapers.adapters.ImageAdapter.*
import uz.pdp.unsplashfullwallpapers.databinding.FragmentPagerBinding
import uz.pdp.unsplashfullwallpapers.model.ImageModel
import uz.pdp.unsplashfullwallpapers.ui.viewmodel.PagerViewModel
import kotlin.coroutines.CoroutineContext

class PagerFragment : Fragment(R.layout.fragment_pager), CoroutineScope {

    private val binding by viewBinding(FragmentPagerBinding::bind)
    lateinit var imageAdapter: ImageAdapter
    private lateinit var pagerViewModel: PagerViewModel
    private val job = Job()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topicId = arguments?.getString("id") ?: ""
        val order = arguments?.getString("order") ?: ""

        pagerViewModel = PagerViewModel(topicId, order)

        imageAdapter = ImageAdapter(object : OnImageClickListener {
            override fun onImageClick(imageModel: ImageModel) {
                findNavController().navigate(R.id.photoFragment, Bundle().apply {
                    putSerializable("image", imageModel)
                })
            }
        })


        launch {
            pagerViewModel.flow.catch {

            }.collect {
                imageAdapter.submitData(it)
            }
        }

        binding.rv.adapter = imageAdapter

    }

    override val coroutineContext: CoroutineContext
        get() = job

}